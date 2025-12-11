package com.example.demo.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class CodeGenerateService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final DecimalFormat FORMAT_6 = new DecimalFormat("2000000"); // 产物流水号（6位）
    private static final DecimalFormat FORMAT_2 = new DecimalFormat("00");   // 任务单序列号（2位）
    private static final DecimalFormat FORMAT_4 = new DecimalFormat("0000");   // 任务单序列号（4位）
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd"); // 日期格式（8位）
    private static final long EXPIRE_DAYS = 1; // 任务单Redis键过期时间（1天）
    /**
     * 生成唯一编码
     * @param Key String类型
     * @return 完整编码（前缀+6位流水号）
     */
    public String generateCode(String Key) {
        // Redis自增（原子操作，初始值1，步长1）
        Long seq = redisTemplate.opsForValue().increment(Key, 1);
        if (seq == null) {
            throw new RuntimeException("编码生成失败：Redis自增异常");
        }
        // 拼接前缀+格式化流水号（如DNA000001、L2000001）
        return Key + FORMAT_6.format(seq);
    }

    /**
     * 任务单编号生成（支持每日序列号重置）
     * @param Key String类型
     * 逻辑：Redis键=key+日期，每日自动生成新键，旧键1天后过期
     */
    public String generateTaskCode(String Key) {
        // 1. 生成当日Redis键（格式：原RedisKey_yyyyMMdd，如seq:task_md_240329）
        String dateStr = LocalDate.now().format(DATE_FORMAT);
        String redisKey = Key + "_" + dateStr;

        // 2. 原子自增：首次访问时键自动创建（初始值1）
        Long seq = redisTemplate.opsForValue().increment(redisKey, 1);
        if (seq == null) throw new RuntimeException("任务单编号生成失败：Redis自增异常");

        // 3. 首次生成时设置过期时间（24小时后自动删除，次日重置序列号）
        if (seq == 1) {
            redisTemplate.expire(redisKey, EXPIRE_DAYS, TimeUnit.DAYS);
        }

        // 4. 拼接：前缀+日期+2位序列号（如MD24032901、DXNGS24032902）
        return Key + dateStr.substring(2) + FORMAT_2.format(seq);
    }

    /**
     * 孔号编号生成
     * @param Key String类型
     * @return 完整编码（前缀+2位流水号）
     */
    public String generateGridCode(String Key) {
        Long seq = redisTemplate.opsForValue().increment(Key, 1);
        if (seq == null) {
            throw new RuntimeException("编码生成失败：Redis自增异常");
        }
        // 拼接前缀+格式化流水号（如A01、B01）
        return Key + FORMAT_2.format(seq);
    }

    /**
     * 冰箱编号生成
     * @param Key String类型
     * @return 完整编码（前缀+1位流水号）
     */
    public String generateOneCode(String Key) {
        Long seq = redisTemplate.opsForValue().increment(Key, 1);
        if (seq == null) {
            throw new RuntimeException("编码生成失败：Redis自增异常");
        }
        // 拼接前缀+格式化流水号（如A01、B01）
        return Key + seq;
    }

    /**
     * 冰箱(中)孔板编号生成
     * @param Key String类型
     * @return 完整编码（前缀+4位流水号）
     */
    public String generateThreeCode(String Key) {
        Long seq = redisTemplate.opsForValue().increment(Key, 1);
        if (seq == null) {
            throw new RuntimeException("编码生成失败：Redis自增异常");
        }
        // 拼接前缀+格式化流水号（如A01、B01）
        return Key + FORMAT_4.format(seq);
    }

}
