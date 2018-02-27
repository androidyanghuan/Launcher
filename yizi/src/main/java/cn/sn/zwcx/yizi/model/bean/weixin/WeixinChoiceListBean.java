package cn.sn.zwcx.yizi.model.bean.weixin;

import java.util.List;

/**
 * Created by on 2018/1/15 11:49.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinChoiceListBean {
    private String reason,error_code;

    private Result result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setErrorCode(String errorCode) {
        this.error_code = errorCode;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WeixinChoiceListBean{" +
                "reason='" + reason + '\'' +
                ", errorCode='" + error_code + '\'' +
                ", result=" + result +
                '}';
    }

    public class Result{
        private List<WeixinChoiceItemBean> list;
        private String totalPage;
        private String ps;
        private String pno;

        public List<WeixinChoiceItemBean> getList() {
            return list;
        }

        public void setList(List<WeixinChoiceItemBean> list) {
            this.list = list;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getPs() {
            return ps;
        }

        public void setPs(String ps) {
            this.ps = ps;
        }

        public String getPno() {
            return pno;
        }

        public void setPno(String pno) {
            this.pno = pno;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "list=" + list +
                    ", totalPage='" + totalPage + '\'' +
                    ", ps='" + ps + '\'' +
                    ", pno='" + pno + '\'' +
                    '}';
        }
    }
}
