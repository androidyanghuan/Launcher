package cn.sn.zwcx.sdk.rx;

/**
 * Created by on 2018/1/16 15:02.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */
public class BusData {
    String id;
    String status;

    public BusData() {
    }

    public BusData(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}