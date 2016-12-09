package com.kwokstudio.fuselage.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 郭垒 on 2016/11/24.
 */

public class ContactInfo implements Parcelable {

    private String name;
    private String phoneNum;
    private String firstChar;
    
    
    /**
     * 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
     * android.os.BadParcelableException:
     * Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class //
     * 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
     * 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
     * 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
     * 5.反序列化对象
     *
     */
    private static final Parcelable.Creator<ContactInfo> CREATOR=new Creator<ContactInfo>() {
        @Override
        public ContactInfo createFromParcel(Parcel parcel) {
            //必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ContactInfo contact=new ContactInfo();
            contact.setName(parcel.readString());
            contact.setPhoneNum(parcel.readString());
            contact.setFirstChar(parcel.readString());
            return contact;
        }

        @Override
        public ContactInfo[] newArray(int i) {
            return new ContactInfo[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        parcel.writeString(name);
        parcel.writeString(phoneNum);
        parcel.writeString(firstChar);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}
