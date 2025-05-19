package com.xiaomi.domain.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class VehicleIdGenerator {

    public String generateVid(Long carId) {
        try {
            byte[] carIdBytes = String.valueOf(carId).getBytes();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(carIdBytes);

            long hash = 0;
            for (int i = 0; i < 12; i++) {
                hash = (hash << 8) | (hashBytes[i] & 0xFF);
            }

            return toBase62(hash, 16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成车辆ID失败", e);
        }
    }

    private String toBase62(long number, int length) {
        final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();

        while (number > 0 && sb.length() < length) {
            sb.append(BASE62_CHARS.charAt((int) (number % 62)));
            number /= 62;
        }

        while (sb.length() < length) {
            sb.insert(0, '0');
        }

        return sb.toString();
    }
}