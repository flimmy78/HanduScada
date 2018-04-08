package main.com.handu.scada.utils;

import main.com.handu.scada.business.dtu.DtuUpdateUtil;
import main.com.handu.scada.config.Config;
import main.com.handu.scada.quartz.QuartzManager;
import main.com.handu.scada.quartz.utils.DtuCommand;

import java.util.ArrayList;

/**
 * Created by 柳梦 on 2018/01/11.
 */
public class Command {

    private static final ArrayList<String> menuList = new ArrayList<String>() {{
        add("1." + MENU);
        add("2." + UPDATE);
        add("3." + CMD);
        add("4." + DEBUG);
        add("5." + QUARTZ);
        add("6." + ENCRYPT);
        add("7." + DECRYPT);
        add("8." + DTU_INFO);
        add("9." + RESTART);
        add("10." + NUMBER);
        add("11." + BROAD_CAST_TIME);
        add("12." + SQL);
    }};
    private static final ArrayList<String> cmdList = new ArrayList<String>() {{
        add("cmd-1000(baseData)");
        add("cmd-1001(dateTime)");
        add("cmd-1002(runState)");
        add("cmd-1003(tripEventRecord)");
        add("cmd-1004(readControlWord)");
        add("cmd-1005(temperature)");
        add("cmd-1006(fallSwitch)");
        add("cmd-1007(readPostalAddress)");
    }};

    private static final String MENU = "menu";

    private static final String CMD = "cmd";
    private static final String CMD_Y = "cmd-*";

    private static final String DEBUG = "log";
    private static final String DEBUG_Y = "l-o";
    private static final String DEBUG_N = "l-c";
    private static final String DEBUG_A = "l-a";

    private static final String QUARTZ = "quartz";
    private static final String QUARTZ_Y = "q-o";
    private static final String QUARTZ_N = "q-c";
    private static final String QUARTZ_R = "q-r";

    private static final String UPDATE = "update";
    private static final String UPDATE_N = "update-s";
    private static final String UPDATE_Y = "update-*";

    private static final String DECRYPT = "dec";
    private static final String DECRYPT_Y = "dec-*";

    private static final String ENCRYPT = "enc";
    private static final String ENCRYPT_Y = "enc-*";

    public static final String START_SQL = "s";
    public static final String START_NOSQL = "n";

    private static final String DTU_RESTART_Y = "rs-*";

    private static final String DTU_INFO = "info";
    private static final String DTU_INFO_Y = "if-*";

    private static final String NUMBER = "num";

    private static final String INFO = "info";
    private static final String RESTART = "restart";

    private static final String SQL = "sql";

    private static final String SQL_Y = "sql-*";

    private static final String BROAD_CAST_TIME = "broadcastTime(bt-address)";
    private static final String TIME = "bt-*";

    public static void command(String command) {
        switch (command) {
            case MENU:
                String menu = "";
                for (String s : menuList) {
                    if (!menu.equals("")) menu += ",\n";
                    else menu += "\n";
                    menu += s;
                }
                LogUtils.info(menu, true);
                return;
            case CMD:
                String cmd = "";
                for (String s : cmdList) {
                    if (!cmd.equals("")) cmd += ",\n";
                    else cmd += "\n";
                    cmd += s;
                }
                LogUtils.info(cmd, true);
                return;
            case DEBUG_Y:
                Config.isDebug = true;
                LogUtils.info("log has " + (Config.isDebug ? "opened" : "closed"), true);
                return;
            case DEBUG:
                LogUtils.info("log has " + (Config.isDebug ? "opened" : "closed") + ",l-(o)pen,l-(c)losesss", true);
                return;
            case DEBUG_A:
                LogUtils.filterAddress = null;
                return;
            case DEBUG_N:
                Config.isDebug = false;
                LogUtils.info("log has " + (Config.isDebug ? "opened" : "closed"), true);
                return;
            case QUARTZ:
                LogUtils.info("quartz is " + (QuartzManager.getInstance().isStart() ? "opened" : "closed") + ",q-(o)pen,q-(c)lose,q-(r)start", true);
                return;
            case QUARTZ_Y:
                if (QuartzManager.getInstance().start())
                    LogUtils.info("quartz has " + (QuartzManager.getInstance().isStart() ? "opened" : "closed"), true);
                return;
            case QUARTZ_N:
                if (QuartzManager.getInstance().stop())
                    LogUtils.info("quartz has " + (QuartzManager.getInstance().isStart() ? "opened" : "closed"), true);
                return;
            case QUARTZ_R:
                if (QuartzManager.getInstance().restart())
                    LogUtils.info("quartz is restart: state has " + (QuartzManager.getInstance().isStart() ? "opened" : "closed"), true);
                return;
            case NUMBER:
                DtuCommand.getInstance().getClientNumber();
                return;
            case UPDATE_N:
                DtuUpdateUtil.stop();
                return;
            case UPDATE:
                LogUtils.info("input update-(500-10000) to update,(s)top to stop", true);
                return;
            case ENCRYPT:
                LogUtils.info("input enc-str", true);
                return;
            case DECRYPT:
                LogUtils.info("input dec-str", true);
                return;
            case INFO:
                LogUtils.info("input if-address", true);
                return;
            case RESTART:
                LogUtils.info("input rs-address", true);
                return;
            case SQL:
                LogUtils.info("input sql-sql", true);
                return;
        }

        if (isMatch(command, UPDATE_Y)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            try {
                Integer interval = Integer.parseInt(command);
                if (interval >= 500 && interval <= 10000) {
                    if (DtuUpdateUtil.verify()) {
                        if (!DtuUpdateUtil.isUpdating) {
                            DtuUpdateUtil.update();
                        } else {
                            LogUtils.error("the update command has been sent...", true);
                        }
                    }
                } else {
                    LogUtils.error("intervalTime error,intervalTime must between 500 and 10000!", true);
                }
            } catch (NumberFormatException e) {
                LogUtils.error("intervalTime error,intervalTime must be integer!", true);
            }
            return;
        }

        if (isMatch(command, CMD_Y)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            try {
                Integer i = Integer.valueOf(command);
                DtuCommand.getInstance().sendByValue(i);
            } catch (NumberFormatException e) {
                LogUtils.error("cmd type error,please retry", true);
            }
            return;
        }

        if (isMatch(command, ENCRYPT_Y)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            if (!StringsUtils.isEmpty(command)) {
                LogUtils.info("encrypt str-->" + AesUtils.encrypt(command), true);
            }
            return;
        }

        if (isMatch(command, DECRYPT_Y)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            if (!StringsUtils.isEmpty(command)) {
                LogUtils.info("decrypt str-->" + AesUtils.decrypt(command), true);
            }
            return;
        }

        if (isMatch(command, DTU_INFO_Y)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            if (!StringsUtils.isEmpty(command)) {
                String dtuAddresses[] = command.split(",");
                if (dtuAddresses.length > 0) {
                    DtuCommand.getInstance().readDtuInfo(dtuAddresses);
                } else {
                    LogUtils.error("input error!", true);
                }
            }
            return;
        }

        if (isMatch(command, DTU_RESTART_Y)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            if (!StringsUtils.isEmpty(command)) {
                String dtuAddresses[] = command.split(",");
                if (dtuAddresses.length > 0) {
                    DtuCommand.getInstance().restartDtu(dtuAddresses);
                } else {
                    LogUtils.error("input error!", true);
                }
            }
            return;
        }

        if (isMatch(command, SQL_Y)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            if (!StringsUtils.isEmpty(command)) {
                if (command.length() > 0) {
                    DtuCommand.getInstance().executeSql(command);
                } else {
                    LogUtils.error("sql input error!", true);
                }
            }
            return;
        }

        if (isMatch(command, TIME)) {
            command = command.substring(command.indexOf("-") + 1, command.length());
            if (!StringsUtils.isEmpty(command)) {
                String dtuAddresses[] = command.split(",");
                if (dtuAddresses.length > 0) {
                    DtuCommand.getInstance().broadcastTime(dtuAddresses);
                } else {
                    LogUtils.error("input error!", true);
                }
            }
            return;
        }
        LogUtils.error("command not find!", true);
    }

    private static boolean isMatch(String s, String p) {
        int i = 0;
        int j = 0;
        int starIndex = -1;
        int iIndex = -1;
        while (i < s.length()) {
            if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
                ++i;
                ++j;
            } else if (j < p.length() && p.charAt(j) == '*') {
                starIndex = j;
                iIndex = i;
                j++;
            } else if (starIndex != -1) {
                j = starIndex + 1;
                i = iIndex + 1;
                iIndex++;
            } else {
                return false;
            }
        }
        while (j < p.length() && p.charAt(j) == '*') {
            ++j;
        }
        return j == p.length();
    }
}