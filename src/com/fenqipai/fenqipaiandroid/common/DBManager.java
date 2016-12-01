package com.fenqipai.fenqipaiandroid.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.base.BaseModel;
import com.fenqipai.fenqipaiandroid.model.BankCardList;
import com.fenqipai.fenqipaiandroid.model.BankList;
import com.fenqipai.fenqipaiandroid.model.Commodity;
import com.fenqipai.fenqipaiandroid.model.HomeLunboList;
import com.fenqipai.fenqipaiandroid.model.IndexNewsList;
import com.fenqipai.fenqipaiandroid.model.InstallmentCar;
import com.fenqipai.fenqipaiandroid.model.LoanAlgorithmList;
import com.fenqipai.fenqipaiandroid.model.MoneyChangeRecord;
import com.fenqipai.fenqipaiandroid.model.Moneyrecord;
import com.fenqipai.fenqipaiandroid.model.MyLocation;
import com.fenqipai.fenqipaiandroid.model.NewsList;
import com.fenqipai.fenqipaiandroid.model.Notice;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.model.OrderList;
import com.fenqipai.fenqipaiandroid.model.PayInfo;
import com.fenqipai.fenqipaiandroid.model.PaymentSaleLoan;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.model.SingleCarDetails;
import com.fenqipai.fenqipaiandroid.model.SingleSaleCarDetails;
import com.fenqipai.fenqipaiandroid.model.StagingBill;
import com.fenqipai.fenqipaiandroid.model.StagingBillDetails;
import com.fenqipai.fenqipaiandroid.model.UserInfo;
import com.fenqipai.fenqipaiandroid.util.AppUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import android.content.Context;
import android.database.Cursor;

/**
 * 数据库访问的公共类
 * 
 * @name DBManager
 * @author liuchengbao
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
public class DBManager {

	private DbUtils db;


	public DBManager(Context context) {

		// 数据库版本跟着apk版本走
		int dbVersion = AppUtils.getVersion(context).versionCode;

		// 创建数据库
		db = DbUtils.create(context, Contants.DATABASE_NAME, dbVersion, new DbUpgradeListener() {

			@Override
			public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
				if (oldVersion < newVersion) {
					updateDb(dbUtils, "Commodity");
					updateDb(dbUtils, "Notice");
					updateDb(dbUtils, "UserInfo");
					updateDb(dbUtils, "CarInfo");
					updateDb(dbUtils, "MyLocation");
				}

			}
		});

		// 开启事物 支持多线程操作数据库
		db.configAllowTransaction(true);

	}
	

	/**
	 * 修改数据库
	 * 
	 * @title updateDb
	 * @author zhaoqingyang
	 * @param db
	 * @param tableName
	 */
	@SuppressWarnings("unchecked")
	private static void updateDb(DbUtils db, String tableName) {

		try {

			Class<BaseModel> c = (Class<BaseModel>) Class.forName("com.fenqipai.fenqipaiandroid.model." + tableName);// 把要使用的类加载到内存中,并且把有关这个类的所有信息都存放到对象c中

			if (db.tableIsExist(c)) {

				List<String> dbFildsList = new ArrayList<String>();

				tableName = "com_fenqipai_fenqipaiandroid_model_" + tableName;

				String str = "select * from " + tableName;

				Cursor cursor = db.execQuery(str);

				int count = cursor.getColumnCount();

				for (int i = 0; i < count; i++) {

					dbFildsList.add(cursor.getColumnName(i));

				}

				cursor.close();

				Field f[] = c.getDeclaredFields();// 把属性的信息提取出来，并且存放到field类的对象中，因为每个field的对象只能存放一个属性的信息所以要用数组去接收

				for (int i = 0; i < f.length; i++) {

					String fildName = f[i].getName();

					if (fildName.equals("serialVersionUID")) {

						continue;

					}

					String fildType = f[i].getType().toString();

					if (!dbFildsList.contains(fildName)) {

						if (fildType.equals("class java.lang.String")) {

							db.execNonQuery("alter table " + tableName + " add " + fildName + " TEXT ");

						} else if (fildType.equals("int") || fildType.equals("long") || fildType.equals("boolean")) {

							db.execNonQuery("alter table " + tableName + " add " + fildName + " INTEGER ");

						}

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 保存我的个人信息
	 * 
	 * @title saveMyInfo
	 * @author liuchengbao
	 * @param info
	 */
	public void saveMyInfo(UserInfo info) {
		try {
			UserInfo old = db.findFirst(Selector.from(UserInfo.class).where("sysId", "=", info.getSysId()));
			if (old != null) {
				db.deleteAll(UserInfo.class);
				info.setId(old.getId());
			}
			db.saveOrUpdate(info);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<Commodity> getCommodityList() {

		List<Commodity> list = null;
		try {
			list = db.findAll(Selector.from(Commodity.class).orderBy("updateTime", true));
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (list == null) {
			list = new ArrayList<Commodity>();
		}
		return list;

	}
	
	/**
	 * 删除全部咨询
	 * 
	 * @title deleteAllExpressList
	 * @author zhaoqingyang
	 * @param areaId
	 */
	public void deleteAllCommoditysList(String areaId) {
		try {

			db.delete(Commodity.class, WhereBuilder.b("areaId", "=", areaId));

		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 保存速递信息
	 * 
	 * @title saveExpressInfo
	 * @author zhaoqingyang
	 * @param expressList
	 */
	public void saveCommodityInfo(Commodity commodity) {
		try {
			Commodity old = db.findFirst(Selector.from(Commodity.class).where(WhereBuilder.b("sysID", "=", commodity.getSysId())));

			if (old != null) {
				commodity.setId(old.getId());
			}

			db.saveOrUpdate(commodity);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
	

	/**
	 * 删除全部的轮播图
	 * 
	 * @title delAllNotice
	 * @author zhaoqingyang
	 */
	public void delAllNotice() {
		try {
			db.deleteAll(Notice.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存首页轮播信息
	 * 
	 * @title saveNotice
	 * @author zhaoqingyang
	 * @param notice
	 */
	public void saveNotice(Notice notice) {
		try {
			Notice old = db
					.findFirst(Selector.from(Notice.class).where(WhereBuilder.b("sysId", "=", notice.getSysId())));

			if (old != null) {
				notice.setId(old.getId());
			}

			db.saveOrUpdate(notice);
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 获取四个首页轮播
	 * 
	 * @title getNoticeFour
	 * @author zhaoqingyang
	 * @return
	 */
	public List<Notice> getNoticeFour() {
		List<Notice> list = null;
		try {
			list = db.findAll(Selector.from(Notice.class).orderBy("noticeNum", true).limit(4));
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (list == null) {
			list = new ArrayList<Notice>();
		}
		return list;
	}
	
	/**
	 * 获取车辆详细信息
	 * 
	 * @title getAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public UserInfo getUserInfo(String id) {

		UserInfo userInfo = null;
		try {
			userInfo = db.findFirst(Selector.from(UserInfo.class).where(WhereBuilder.b("sysId", "=", id)));
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (userInfo == null) {
			userInfo = new UserInfo();
		}
		return userInfo;

	}
	
	
	
	/**
	 * 获取拍卖列表 
	 * 
	 * @title getAuctionList
	 * @author qianyuhang
	 * @param notice
	 */
	public List<SaleCar> getAuctionList(int curRows,int pageSize) {
			List<SaleCar> list = null;
			try {
				list = db.findAll(Selector.from(SaleCar.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<SaleCar>();
			}
			return list;
	}
	/**
	 * 删除拍卖列表 
	 * 
	 * @title getAuctionList
	 * @author qianyuhang
	 * @param notice
	 */
	public void deleteAllAuctionList() {
		try {
			db.deleteAll(SaleCar.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存拍卖
	 * 
	 * @title getAuctionList
	 * @author qianyuhang
	 * @param notice
	 */
	public void saveAuction(SaleCar saleCar) {
		try {
			SaleCar old = db.findFirst(Selector.from(SaleCar.class).where(WhereBuilder.b("sysID", "=", saleCar.getSysId())));

			if (old != null) {
				saleCar.setId(old.getId());
			}

			db.saveOrUpdate(saleCar);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除车辆详细信息 
	 * 
	 * @title deleteAllAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public void deleteAllAuctionInfo() {
		try {
			db.deleteAll(SingleCarDetails.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存车辆详细信息
	 * 
	 * @title saveAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public void saveAuctionInfo(SingleCarDetails singleCarDetails) {
		try {
			SingleCarDetails old = db.findFirst(Selector.from(SingleCarDetails.class).where(WhereBuilder.b("sysID", "=", singleCarDetails.getSysId())));

			if (old != null) {
				singleCarDetails.setId(old.getId());
			}

			db.saveOrUpdate(singleCarDetails);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取车辆详细信息
	 * 
	 * @title getAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public SingleCarDetails getAuctionInfo(String auctionId) {

		SingleCarDetails singleCarDetails = null;
		try {
			singleCarDetails = db.findFirst(Selector.from(SingleCarDetails.class).where("sysID", "=", auctionId));
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (singleCarDetails == null) {
			singleCarDetails = new SingleCarDetails();
		}
		return singleCarDetails;

	}
	
	/**
	 * 获取拍卖列表 
	 * 
	 * @title getInstallmentList
	 * @author hunaixin
	 */
	public List<InstallmentCar> getInstallmentList(int curRows,int pageSize) {
			List<InstallmentCar> list = null;
			try {
				list = db.findAll(Selector.from(InstallmentCar.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<InstallmentCar>();
			}
			return list;
	}
	/**
	 * 删除拍卖列表 
	 * 
	 * @title deleteInstallmentList
	 * @author hunaixin
	 */
	public void deleteAllInstallmentList() {
		try {
			db.deleteAll(InstallmentCar.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存拍卖
	 * 
	 * @title saveInstallmentList
	 * @author hunaixin
	 */
	public void saveInstallmentList(InstallmentCar installmentCar) {
		try {
			InstallmentCar old = db.findFirst(Selector.from(InstallmentCar.class).where(WhereBuilder.b("sysID", "=", installmentCar.getSysId())));

			if (old != null) {
				installmentCar.setId(old.getId());
			}

			db.saveOrUpdate(installmentCar);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除分期车辆详细信息 
	 * 
	 * @title deleteAllAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public void deleteAllSaleInfo() {
		try {
			db.deleteAll(SingleSaleCarDetails.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存分期车辆详细信息
	 * 
	 * @title saveAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public void saveSaleInfo(SingleSaleCarDetails singleSaleCarDetails) {
		try {
			SingleSaleCarDetails old = db.findFirst(Selector.from(SingleSaleCarDetails.class).where(WhereBuilder.b("sysID", "=", singleSaleCarDetails.getSysId())));

			if (old != null) {
				singleSaleCarDetails.setId(old.getId());
			}

			db.saveOrUpdate(singleSaleCarDetails);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取分期车辆详细信息
	 * 
	 * @title getAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public SingleSaleCarDetails getsaleInfo(String saleId) {

		SingleSaleCarDetails singleSaleCarDetails = null;
		try {
			singleSaleCarDetails = db.findFirst(Selector.from(SingleSaleCarDetails.class).where("sysID", "=", saleId));
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (singleSaleCarDetails == null) {
			singleSaleCarDetails = new SingleSaleCarDetails();
		}
		return singleSaleCarDetails;

	}
	
	/**
	 * 获取订单列表 
	 * 
	 * @title getOrderList
	 * @author hunaixin
	 */
	public List<OrderList> getOrderList(int curRows,int pageSize) {
			List<OrderList> list = null;
			try {
				list = db.findAll(Selector.from(OrderList.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<OrderList>();
			}
			return list;
	}
	
	/**
	 * 删除订单列表
	 * 
	 * @title deleteOrderList
	 * @author hunaixin
	 */
	public void deleteAllOrderList() {
		try {
			db.deleteAll(OrderList.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存订单列表
	 * 
	 * @title saveOrderList
	 * @author hunaixin
	 */
	public void saveOrderList(OrderList orderList) {
		try {
			OrderList old = db.findFirst(Selector.from(OrderList.class).where(WhereBuilder.b("sysID", "=", orderList.getSysId())));

			if (old != null) {
				orderList.setId(old.getId());
			}

			db.saveOrUpdate(orderList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除车辆详细信息 
	 * 
	 * @title deleteAllAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public void deleteAllOrderInfo() {
		try {
			db.deleteAll(OrderInfo.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存订单详细信息
	 * 
	 * @title saveAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public void saveOrderInfo(OrderInfo orderInfo) {
		try {
			OrderInfo old = db.findFirst(Selector.from(OrderInfo.class).where(WhereBuilder.b("sysID", "=", orderInfo.getSysId())));
			if (old != null) {
				orderInfo.setId(old.getId());
			}

			db.saveOrUpdate(orderInfo);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取订单详细信息
	 * 
	 * @title getAuctionInfo
	 * @author hunaixin
	 * 
	 */
	public OrderInfo getOrderInfo(String orderId) {

		OrderInfo orderInfo = null;
		try {
			orderInfo = db.findFirst(Selector.from(OrderInfo.class).where("sysID", "=", orderId));
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (orderInfo == null) {
			orderInfo = new OrderInfo();
		}
		return orderInfo;

	}
	
	/**
	 * 获取支持银行列表 
	 * 
	 * @title getBankList
	 * @author hunaixin
	 */
	public List<BankList> getBankList() {
			List<BankList> list = null;
			try {
				list = db.findAll(Selector.from(BankList.class));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<BankList>();
			}
			return list;
	}
	
	/**
	 * 删除支持银行列表
	 * 
	 * @title deleteBankList
	 * @author hunaixin
	 */
	public void deleteAllBankList() {
		try {
			db.deleteAll(BankList.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存支持银行列表
	 * 
	 * @title saveBankList
	 * @author hunaixin
	 */
	public void saveBankList(BankList bankList) {
		try {
			BankList old = db.findFirst(Selector.from(BankList.class).where(WhereBuilder.b("sysID", "=", bankList.getSysId())));

			if (old != null) {
				bankList.setId(old.getId());
			}

			db.saveOrUpdate(bankList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取绑定银行卡列表 
	 * 
	 * @title getBankCardList
	 * @author hunaixin
	 */
	public List<BankCardList> getBankCardList() {
			List<BankCardList> list = null;
			try {
				list = db.findAll(Selector.from(BankCardList.class));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<BankCardList>();
			}
			return list;
	}
	
	/**
	 * 删除绑定银行卡列表
	 * 
	 * @title deleteBankCardList
	 * @author hunaixin
	 */
	public void deleteAllBankCardList() {
		try {
			db.deleteAll(BankCardList.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存绑定银行卡列表
	 * 
	 * @title saveBankCardList
	 * @author hunaixin
	 */
	public void saveBankCardList(BankCardList bankCardList) {
		try {
			BankCardList old = db.findFirst(Selector.from(BankCardList.class).where(WhereBuilder.b("sysID", "=", bankCardList.getSysId())));

			if (old != null) {
				bankCardList.setId(old.getId());
			}

			db.saveOrUpdate(bankCardList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取销售方案确认查询列表 
	 * 
	 * @title PaymentSaleLoan
	 * @author hunaixin
	 */
	public List<PaymentSaleLoan> getPaymentSaleLoanList() {
			List<PaymentSaleLoan> list = null;
			try {
				list = db.findAll(Selector.from(PaymentSaleLoan.class));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<PaymentSaleLoan>();
			}
			return list;
	}
	
	/**
	 * 删除销售方案确认查询列表
	 * 
	 * @title deletePaymentSaleLoanList
	 * @author hunaixin
	 */
	public void deleteAllPaymentSaleLoanList() {
		try {
			db.deleteAll(PaymentSaleLoan.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存销售方案确认查询列表
	 * 
	 * @title PaymentSaleLoan
	 * @author hunaixin
	 */
	public void savePaymentSaleLoanList(PaymentSaleLoan paymentSaleLoan) {
		try {
			PaymentSaleLoan old = db.findFirst(Selector.from(PaymentSaleLoan.class).where(WhereBuilder.b("sysID", "=", paymentSaleLoan.getSysId())));

			if (old != null) {
				paymentSaleLoan.setId(old.getId());
			}

			db.saveOrUpdate(paymentSaleLoan);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取分期业务获取对应信息 
	 * 
	 * @title PaymentSaleLoan
	 * @author hunaixin
	 */
	public List<LoanAlgorithmList> getLoanAlgorithmList() {
			List<LoanAlgorithmList> list = null;
			try {
				list = db.findAll(Selector.from(LoanAlgorithmList.class));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<LoanAlgorithmList>();
			}
			return list;
	}
	
	/**
	 * 删除分期业务获取对应信息
	 * 
	 * @title deletePaymentSaleLoanList
	 * @author hunaixin
	 */
	public void deleteAllLoanAlgorithmListt() {
		try {
			db.deleteAll(LoanAlgorithmList.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存分期业务获取对应信息
	 * 
	 * @title PaymentSaleLoan
	 * @author hunaixin
	 */
	public void saveLoanAlgorithmList(LoanAlgorithmList loanAlgorithmList) {
		try {
			LoanAlgorithmList old = db.findFirst(Selector.from(LoanAlgorithmList.class).where(WhereBuilder.b("sysID", "=", loanAlgorithmList.getSysId())));

			if (old != null) {
				loanAlgorithmList.setId(old.getId());
			}

			db.saveOrUpdate(loanAlgorithmList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存支付信息列表
	 * 
	 * @title savePayInfo
	 * @author wangZhonghan
	 */
	public void savePayInfo(PayInfo payInfo) {
		try {
			PayInfo old = db.findFirst(Selector.from(PayInfo.class).where(WhereBuilder.b("sysID", "=", payInfo.getSysId())));

			if (old != null) {
				payInfo.setId(old.getId());
			}

			db.saveOrUpdate(payInfo);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除支付信息列表
	 * 
	 * @title deleteOrderList
	 * @author wangZhonghan
	 */
	public void deletePayInfo() {
		try {
			db.deleteAll(PayInfo.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取支付信息列表 
	 * 
	 * @title getPayInfo
	 * @author wangZhonghan
	 */
	public List<PayInfo> getPayInfo(int curRows,int pageSize) {
			List<PayInfo> list = null;
			try {
				list = db.findAll(Selector.from(PayInfo.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<PayInfo>();
			}
			return list;
	}
	

	/**
	 * 获取资金记录 
	 * 
	 * @title getOrderList
	 * @author hunaixin
	 */
	public List<Moneyrecord> getBailList(int curRows,int pageSize) {
			List<Moneyrecord> list = null;
			try {
				list = db.findAll(Selector.from(Moneyrecord.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<Moneyrecord>();
			}
			return list;
	}
	/**
	 * 删除资金记录 
	 * 
	 * @title deleteOrderList
	 * @author hunaixin
	 */
	public void deleteAllBailList() {
		try {
			db.deleteAll(Moneyrecord.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存资金记录 
	 * 
	 * @title saveOrderList
	 * @author hunaixin
	 */
	public void saveBailList(Moneyrecord moneyrecord) {
		try {
			Moneyrecord old = db.findFirst(Selector.from(Moneyrecord.class).where(WhereBuilder.b("sysID", "=", moneyrecord.getSysId())));

			if (old != null) {
				moneyrecord.setId(old.getId());
			}

			db.saveOrUpdate(moneyrecord);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取分期订单列表 
	 * 
	 * @title getOrderList
	 * @author hunaixin
	 */
	public List<StagingBill> getRepaymentOrderList(int curRows,int pageSize) {
			List<StagingBill> list = null;
			try {
				list = db.findAll(Selector.from(StagingBill.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<StagingBill>();
			}
			return list;
	}
	/**
	 * 删除分期订单列表 
	 * 
	 * @title deleteOrderList
	 * @author hunaixin
	 */
	public void deleteAllStagingBill() {
		try {
			db.deleteAll(StagingBill.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存分期账单记录 
	 * 
	 * @title saveOrderList
	 * @author hunaixin
	 */
	public void saveStagingBill(StagingBill stagingBill) {
		try {
			StagingBill old = db.findFirst(Selector.from(StagingBill.class).where(WhereBuilder.b("sysID", "=", stagingBill.getSysId())));

			if (old != null) {
				stagingBill.setId(old.getId());
			}

			db.saveOrUpdate(stagingBill);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取新闻列表 
	 * 
	 */
	public List<IndexNewsList> getNewsList() {
			List<IndexNewsList> list = null;
			try {
				list = db.findAll(Selector.from(IndexNewsList.class).limit(4).offset(0));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<IndexNewsList>();
			}
			return list;
	}
	/**
	 * 删除新闻列表 
	 * 
	 */
	public void deleteNewsList() {
		try {
			db.deleteAll(IndexNewsList.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存新闻列表
	 * 
	 */
	public void saveNewsList(IndexNewsList indexNewsList) {
		try {
			IndexNewsList old = db.findFirst(Selector.from(IndexNewsList.class).where(WhereBuilder.b("sysID", "=", indexNewsList.getSysId())));

			if (old != null) {
				indexNewsList.setId(old.getId());
			}

			db.saveOrUpdate(indexNewsList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取首页轮播图列表 
	 * 
	 */
	public List<HomeLunboList> getLunboList() {
			List<HomeLunboList> list = null;
			try {
				list = db.findAll(Selector.from(HomeLunboList.class));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<HomeLunboList>();
			}
			return list;
	}
	/**
	 * 删除首页轮播图列表 
	 * 
	 */
	public void deleteLunboList() {
		try {
			db.deleteAll(HomeLunboList.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存首页轮播图列表
	 * 
	 */
	public void saveLunboList(HomeLunboList homeLunboList) {
		try {
			HomeLunboList old = db.findFirst(Selector.from(HomeLunboList.class).where(WhereBuilder.b("sysID", "=", homeLunboList.getSysId())));

			if (old != null) {
				homeLunboList.setId(old.getId());
			}

			db.saveOrUpdate(homeLunboList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取更多新闻列表 
	 * 
	 * @title getNewsList
	 * @author hunaixin
	 */
	public List<NewsList> getNews(int curRows,int pageSize) {
			List<NewsList> list = null;
			try {
				list = db.findAll(Selector.from(NewsList.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<NewsList>();
			}
			return list;
	}
	
	/**
	 * 删除更多新闻列表
	 * 
	 * @title deleteNewsList
	 * @author hunaixin
	 */
	public void deleteAllNews() {
		try {
			db.deleteAll(NewsList.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存更多新闻列表
	 * 
	 * @title saveOrderList
	 * @author hunaixin
	 */
	public void saveNews(NewsList newsList) {
		try {
			NewsList old = db.findFirst(Selector.from(NewsList.class).where(WhereBuilder.b("sysID", "=", newsList.getSysId())));

			if (old != null) {
				newsList.setId(old.getId());
			}

			db.saveOrUpdate(newsList);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取活动位置坐标 
	 * 
	 * @title getOrderList
	 * @author hunaixin
	 */
	public List<MyLocation> getSeeCarPoint() {
			List<MyLocation> list = null;
			try {
				list = db.findAll(Selector.from(MyLocation.class));
			} catch (DbException e) {
				e.printStackTrace();
			}
			 
			if (list == null) {
				list = new ArrayList<MyLocation>();
			}
			return list;
	}
	
	/**
	 * 删除动位置坐标 
	 * 
	 * @title deleteOrderList
	 * @author hunaixin
	 */
	public void deleteAllSeeCarPoint() {
		try {
			db.deleteAll(MyLocation.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存动位置坐标
	 * 
	 * @title saveOrderList
	 * @author hunaixin
	 */
	public void saveSeeCarPoint(MyLocation myLocation) {
		try {
			MyLocation old = db.findFirst(Selector.from(MyLocation.class).where(WhereBuilder.b("sysID", "=", myLocation.getSysId())));

			if (old != null) {
				myLocation.setId(old.getId());
			}

			db.saveOrUpdate(myLocation);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取分期账单详细列表 
	 * 
	 * @title getOrderList
	 * @author hunaixin
	 */
	public List<StagingBillDetails> getOrderRepaymentList(int curRows,int pageSize) {
			List<StagingBillDetails> list = null;
			try {
				list = db.findAll(Selector.from(StagingBillDetails.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<StagingBillDetails>();
			}
			return list;
	}
	/**
	 * 删除分期账单详细列表 
	 * 
	 * @title deleteOrderList
	 * @author hunaixin
	 */
	public void deleteAllStagingBillDetails() {
		try {
			db.deleteAll(StagingBillDetails.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存分期账单详细 
	 * 
	 * @title saveOrderList
	 * @author hunaixin
	 */
	public void saveStagingBillDetails(StagingBillDetails stagingBillDetails) {
		try {
			StagingBillDetails old = db.findFirst(Selector.from(StagingBillDetails.class).where(WhereBuilder.b("sysID", "=", stagingBillDetails.getSysId())));

			if (old != null) {
				stagingBillDetails.setId(old.getId());
			}

			db.saveOrUpdate(stagingBillDetails);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取资金记录 
	 * 
	 * @title MoneyChangeRecord
	 * @author hunaixin
	 */
	public List<MoneyChangeRecord> getMoneyChangeRecord(int curRows,int pageSize) {
			List<MoneyChangeRecord> list = null;
			try {
				list = db.findAll(Selector.from(MoneyChangeRecord.class).limit(pageSize).offset(curRows));
			} catch (DbException e) {
				e.printStackTrace();
			}

			if (list == null) {
				list = new ArrayList<MoneyChangeRecord>();
			}
			return list;
	}
	/**
	 * 删除资金记录 
	 * 
	 * @title deleteOrderList
	 * @author hunaixin
	 */
	public void deleteAllMoneyChangeRecord() {
		try {
			db.deleteAll(MoneyChangeRecord.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存资金记录 
	 * 
	 * @title saveOrderList
	 * @author hunaixin
	 */
	public void saveBMoneyChangeRecord(MoneyChangeRecord moneyrecord) {
		try {
			MoneyChangeRecord old = db.findFirst(Selector.from(MoneyChangeRecord.class).where(WhereBuilder.b("sysID", "=", moneyrecord.getSysId())));

			if (old != null) {
				moneyrecord.setId(old.getId());
			}

			db.saveOrUpdate(moneyrecord);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
