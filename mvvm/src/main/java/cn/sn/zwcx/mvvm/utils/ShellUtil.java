package cn.sn.zwcx.mvvm.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by on 2018/3/14 10:54.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ShellUtil {

    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private ShellUtil() {
        throw new UnsupportedOperationException("You can't instance " + ShellUtil.class.getSimpleName());
    }

    /**
     * check whether has root permission
     * @return
     */
    public static boolean checkRootPermission(){
        return execCommand("echo root",true,false).result == 0;
    }

    /**
     * execute shell command, default return result msg
     * @param command
     * @param isRoot whether need to run with root
     * @return
     */
    public static CommandResult execCommand(String command, boolean isRoot){
        return execCommand(new String[]{command},isRoot,true);
    }

    /**
     * execute shell commands default return result msg
     * @param commands
     * @param isRoot
     * @return
     */
    public static CommandResult execCommand(List<String> commands,boolean isRoot){
        return execCommand(commands == null ? null : commands.toArray(new String[]{}),isRoot,true);
    }

    /**
     * execute shell commands default return result msg
     * @param commands
     * @param isRoot
     * @return
     */
    public static CommandResult execCommand(String[] commands,boolean isRoot){
        return execCommand(commands,isRoot,true);
    }

    /**
     * execute shell command
     * @param command
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     */
    public static CommandResult execCommand(String command, boolean isRoot,boolean isNeedResultMsg){
        return execCommand(new String[]{command},isRoot,isNeedResultMsg);
    }

    /**
     * execute shell command
     * @param commands
     * @param isRoot
     * @param isNeedResultMsg
     * @return
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg){
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg){
        int result = -1;
        if (commands == null || commands.length == 0)
            return new CommandResult(result,null,null);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuffer successMsg = null;
        StringBuffer errorMsg = null;
        DataOutputStream dos = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            dos = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (TextUtils.isEmpty(command))
                    continue;
                dos.write(command.getBytes());
                dos.writeBytes(COMMAND_LINE_END);
                dos.flush();
            }
            dos.writeBytes(COMMAND_EXIT);
            dos.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuffer();
                errorMsg = new StringBuffer();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null)
                    successMsg.append(s);
                while ((s = errorResult.readLine()) != null)
                    errorMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (dos != null)
                    dos.close();
                if (successResult != null)
                    successResult.close();
                if (errorResult != null)
                    errorResult.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null)
                process.destroy();
        }
        return new CommandResult(result,successMsg == null ? null : successMsg.toString(),errorMsg == null ? null : errorMsg.toString());
    }

    /**
     * result of command
     * <ul>
     * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
     * linux shell</li>
     * <li>{@link CommandResult#successMsg} means success message of command result</li>
     * <li>{@link CommandResult#errorMsg} means error message of command result</li>
     * </ul>
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    public static class CommandResult {

        /** result of command **/
        public int    result;
        /** success message of command result **/
        public String successMsg;
        /** error message of command result **/
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
