package com.example.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class AcmEvidenceFtpJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
       String ret="定时任务成功";
        System.out.println(ret);
        log.info("上传结果: ",ret);
    }


    public static void main(String[] args) {
        String ret="定时任务成功";
        System.out.println(ret);
    }
}
