package com.example.demo.test;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/02/05 17:19
 */

@Slf4j
public class TestSimpleDateFormat {

    public static void main(String[] args) {
        testSimpleDateFormat();
    }

    private static void testSimpleDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");



    }


}
