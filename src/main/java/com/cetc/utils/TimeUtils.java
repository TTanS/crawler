package com.cetc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils
{
  public static String getTime()
  {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    return simpleDateFormat.format(new Date());
  }
}
