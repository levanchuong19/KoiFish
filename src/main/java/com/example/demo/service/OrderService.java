package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.OrderStatus;
import com.example.demo.entity.enums.PaymentEnums;
import com.example.demo.entity.enums.Role;
import com.example.demo.entity.enums.TransactionEnum;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.request.OrderDetailsRequestDTO;
import com.example.demo.model.request.OrderRequestDTO;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    KoiRepository koiRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    public Order create(OrderRequestDTO orderRequestDTO) {
        Account customer = authenticationService.getCurrentAccount();
        Order order = new Order();
        order.setDate(new Date());
        order.setCustomer(customer);
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        float total = 0;
        for (OrderDetailsRequestDTO orderDetailsRequestDTO : orderRequestDTO.getDetails()) {
            OrderDetails orderDetails = new OrderDetails();
            Koi koi = koiRepository.findKoiById(orderDetailsRequestDTO.getKoiId());
            orderDetails.setKoi(koi);
            orderDetails.setQuantity(orderDetailsRequestDTO.getQuantity());
            orderDetails.setPrice(koi.getPrice());
            orderDetails.setOrder(order);
            total += koi.getPrice() * orderDetailsRequestDTO.getQuantity();
            orderDetailsList.add(orderDetails);
        }
        order.setOrderDetails(orderDetailsList);
        order.setTotal(total);
        return orderRepository.save(order);//tự lưu bảng reference đến với bảng order -> bảng details tự lưu ko cần
        // .save
    }

    public String createUrl(OrderRequestDTO orderRequest) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        //code minh`
        Order order = create(orderRequest);
        float amount = order.getTotal() * 100;//khử thập phân theo VNPay's requirement
        String amountStr = String.format("%.0f", amount);//ép về định dạng 0 dấu thập phân và ép về string để xóa dấu . thập phân

        String tmnCode = "GVOIN1TF";
        String secretKey = "JE6NKK6CJRA7V35NCRU3RS2RCFTSJGH3";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:5173/success?orderID=" + order.getId();//trang thong bao thanh cong o Front End
        //https://blearning.vn/guide/swp/docker-local?orderID=1472c79f-07a9-4a8b-8faa-4860cb36c6ba
        //&vnp_Amount=40000000
        //&vnp_BankCode=NCB
        //&vnp_BankTranNo=VNP14612283
        //&vnp_CardType=ATM
        //&vnp_OrderInfo=Thanh+toan+cho+ma+GD%3A+1472c79f-07a9-4a8b-8faa-4860cb36c6ba
        //&vnp_PayDate=20241013120158
        //&vnp_ResponseCode=00 ( 00 : payment succeed)
        //&vnp_TmnCode=GVOIN1TF
        //&vnp_TransactionNo=14612283
        //&vnp_TransactionStatus=00
        //&vnp_TxnRef=1472c79f-07a9-4a8b-8faa-4860cb36c6ba
        //&vnp_SecureHash=edeb6768fc1fc408b03cffb58e32e09d25edf49c238b87f682c1dccbe4f6106ded956e6a80baba6d046c14a2b79225b00001e66bfbac95dda1239d1b38aa0f51
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", order.getId().toString());
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + order.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", amountStr);

        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }

    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public void createTransaction(UUID uuid) {
        // Find the order
        Order order = orderRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(OrderStatus.PAID);
        // Create payment
        Payment payment = new Payment();
        payment.setCreateAt(new Date());
        payment.setOrder(order);
        payment.setMethod(PaymentEnums.BANKING);
        payment.setTotal(order.getTotal());
        // Initialize the transaction set
        Set<Transaction> transactionSet = new HashSet<>();

        // Create transaction 1
        Transaction transaction1 = new Transaction();
        transaction1.setFrom(null); // From VNPay
        Account customer = authenticationService.getCurrentAccount();
        transaction1.setCreateAt(new Date());
        transaction1.setTo(customer);
        transaction1.setPayment(payment);
        transaction1.setStatus(TransactionEnum.SUCCESS);
        transaction1.setDescription("Nạp tiền VNPay to Customer");

        // Add to transaction set
        transactionSet.add(transaction1);

        // Create transaction 2
        Transaction transaction2 = new Transaction();
        Account admin = accountRepository.findAccountByRole(Role.ADMIN);
        transaction2.setCreateAt(new Date());
        transaction2.setFrom(customer);
        transaction2.setTo(admin);
        transaction2.setPayment(payment);
        transaction2.setStatus(TransactionEnum.SUCCESS);
        transaction2.setDescription("CUSTOMER TO ADMIN");

        // Calculate new balance for admin
        float newBalance = admin.getBalance() + order.getTotal() * 0.10f; // Commission
        transaction2.setAmount(newBalance);
        admin.setBalance(newBalance);

        // Add to transaction set
        transactionSet.add(transaction2);

        // Create transaction 3
        Transaction transaction3 = new Transaction();
        transaction3.setPayment(payment);
        transaction3.setCreateAt(new Date());
        transaction3.setStatus(TransactionEnum.SUCCESS);
        transaction3.setDescription("ADMIN TO OWNER");
        transaction3.setFrom(admin);
        Account owner = order.getOrderDetails().get(0).getKoi().getOwner();
        transaction3.setTo(owner);

        // Add to transaction set
        transactionSet.add(transaction3);

        // Calculate new balance for owner
        float newOwnerBalance = owner.getBalance() + order.getTotal() * (1 - 0.10f);
        transaction3.setAmount(newOwnerBalance);
        owner.setBalance(newOwnerBalance);

        // Set the transactions in payment
        payment.setTransactions(transactionSet);

        // Debugging output
        System.out.println("Transactions to be saved: " + payment.getTransactions());

        // Save admin, owner, and payment
        try {
            accountRepository.save(admin);
            accountRepository.save(owner);
            paymentRepository.save(payment); // This should cascade to save all transactions
        } catch (Exception e) {
            System.err.println("Error saving payment or transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
