package com.example.quartz.controller;

import com.alibaba.fastjson.JSON;
import com.example.quartz.entity.JobDto;
import com.example.quartz.entity.Retval;
import com.example.quartz.service.QuartzService;
import com.example.quartz.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/quartz")
public class QuartzController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private Scheduler quartzScheduler;
    @Resource
    private QuartzService quartzService;

    /**
     * 定时列表页
     */
    @ResponseBody
    @RequestMapping(value = "/list.do")
    public ResultData<List<JobDto>> listJob() throws SchedulerException {
        List<JobDto> jobInfos = this.getSchedulerJobInfo();
        return new ResultData(0, "ok", jobInfos.size(), jobInfos);
    }

    @ResponseBody
    @RequestMapping(value = "/list1.do")
    public String listJob1() throws SchedulerException {
        List<JobDto> jobInfos = this.getSchedulerJobInfo();

        return JSON.toJSONString(jobInfos);
    }
    @ResponseBody
    @RequestMapping(value = "/find.do")
    public ResultData<List<JobDto>> find(JobDto jobDto) throws SchedulerException {
        List<JobDto> jobInfos = this.getSchedulerJobInfo();
        String jobname=jobDto.getJobName();
        String jobstatus=jobDto.getJobStatus();
        List<JobDto> handle = jobInfos.stream()
                .filter(p -> p.getJobName().equals(jobname))
                .collect(Collectors.toList());

        return new ResultData(0, "ok", jobInfos.size(), jobInfos);
    }
    /**
     * 新建job
     */
    @ResponseBody
    @RequestMapping(value = "/add.do")
    public Retval save(JobDto jobDto) {
        System.out.println(jobDto);
        Retval retval = Retval.newInstance();
        try {
            quartzService.addJob(jobDto.getJobName(), jobDto.getJobGroupName(), jobDto.getTriggerName(), jobDto.getTriggerGroupName(), Class.forName(jobDto.getJobClass()), jobDto.getCronExpression());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return retval;
    }

    /**
     * 编辑job
     */
    @ResponseBody
    @RequestMapping(value = "/edit.do", method = RequestMethod.POST)
    public Retval edit(JobDto jobDto) {
        Retval retval = Retval.newInstance();
        try {
            boolean result = quartzService.modifyJobTime(jobDto.getOldJobName(), jobDto.getOldJobGroupName(), jobDto.getOldTriggerName(), jobDto.getOldTriggerGroupName(), jobDto.getJobName(), jobDto.getJobGroupName(), jobDto.getTriggerName(), jobDto.getTriggerGroupName(), jobDto.getCronExpression());
            if (result) {
                retval.put("message", "修改任务成功!");
            } else {
                retval.put("message", "修改任务失败!");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return retval;
    }

    /**
     * 暂停job
     */
    @ResponseBody
    @RequestMapping(value = "/stopJob.do", method = RequestMethod.POST)
    public Retval stopJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName) {
        Retval retval = Retval.newInstance();
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName)) {
            retval.fail();
            retval.put("message", "暂停失败");
        } else {
            try {
                quartzService.pauseJob(jobName, jobGroupName);
                retval.put("message", "暂停成功");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

        }
        return retval;
    }

    /**
     * 恢复job
     */
    @ResponseBody
    @RequestMapping(value = "/resumeJob.do", method = RequestMethod.POST)
    public Retval resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName) {
        Retval retval = Retval.newInstance();
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName)) {
            retval.fail();
            retval.put("message", "恢复失败");
        } else {
            try {
                quartzService.resumeJob(jobName, jobGroupName);
                retval.put("message", "恢复成功");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return retval;
    }

    /**
     * 删除job
     */
    @RequestMapping(value = "/deleteJob.do", method = RequestMethod.POST)
    @ResponseBody
    public Retval deleteJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName, @RequestParam("triggerName") String triggerName, @RequestParam("triggerGroupName") String triggerGroupName) {
        Retval retval = Retval.newInstance();

        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName) || StringUtils.isEmpty(triggerName) || StringUtils.isEmpty(triggerGroupName)) {
            retval.fail();
            retval.put("message", "删除失败");
        } else {
            quartzService.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
            retval.put("message", "删除成功");
        }
        return retval;
    }

    private List<JobDto> getSchedulerJobInfo() throws SchedulerException {
        List<JobDto> jobInfos = new ArrayList<>();
        List<String> triggerGroupNames = quartzScheduler.getTriggerGroupNames();
        for (String triggerGroupName : triggerGroupNames) {
            Set<TriggerKey> triggerKeySet = quartzScheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName));
            for (TriggerKey triggerKey : triggerKeySet) {
                Trigger t = quartzScheduler.getTrigger(triggerKey);
                if (t instanceof CronTrigger) {
                    CronTrigger trigger = (CronTrigger) t;
                    JobKey jobKey = trigger.getJobKey();
                    JobDetail jd = quartzScheduler.getJobDetail(jobKey);
                    JobDto jobInfo = new JobDto();
                    jobInfo.setJobName(jobKey.getName());
                    jobInfo.setJobGroupName(jobKey.getGroup());
                    jobInfo.setTriggerName(triggerKey.getName());
                    jobInfo.setTriggerGroupName(triggerKey.getGroup());
                    jobInfo.setCronExpression(trigger.getCronExpression());
                    jobInfo.setNextFireTime(trigger.getNextFireTime());
                    jobInfo.setPreviousFireTime(trigger.getPreviousFireTime());
                    jobInfo.setStartTime(trigger.getStartTime());
                    jobInfo.setEndTime(trigger.getEndTime());
                    jobInfo.setJobClass(jd.getJobClass().getCanonicalName());
                    // jobInfo.setDuration(Long.parseLong(jd.getDescription()));
                    Trigger.TriggerState triggerState = quartzScheduler.getTriggerState(trigger.getKey());
                    jobInfo.setJobStatus(triggerState.toString());// NONE无,
                    // NORMAL正常,
                    // PAUSED暂停,
                    // COMPLETE完全,
                    // ERROR错误,
                    // BLOCKED阻塞
                    JobDataMap map = quartzScheduler.getJobDetail(jobKey).getJobDataMap();
                    if (null != map && map.size() != 0) {
                        jobInfo.setCount(Long.valueOf((String) map.get("count")));
                        jobInfo.setJobDataMap(map);
                    } else {
                        jobInfo.setJobDataMap(new JobDataMap());
                    }
                    jobInfos.add(jobInfo);
                }
            }
        }
        return jobInfos;
    }
}
