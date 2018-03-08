package com.cetc.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil
{
  public static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
  
  @Test
  public void testlog()
  {
    logger.debug("aThis is debug message");
    logger.info("aThis is info message");
    logger.warn("aThis is warn message");
    logger.error("aThis is error message");
  }
}
