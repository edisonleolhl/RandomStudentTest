package com.lhl.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lhl.db.DBUtil;

public class RandomStudentTest {
	
	public static void main(String[] args) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String sql = "SELECT student_id, name, status FROM student WHERE student_id = ? ";
		//生成3个不等随机数，取值范围[1,40]
		List<Integer> randomNumList = createRandomNumList(40,3);
		String name = null;
		String status = null;
		ResultSet rs = null;
		PreparedStatement ptmt = conn.prepareStatement(sql);
		for(int i = 0; i < 3; i++){
			ptmt.setInt(1, randomNumList.get(i));
			rs = ptmt.executeQuery();
			while(rs.next()){
				name = rs.getString("name");
				status = rs.getString("status");
			}
			System.out.println(randomNumList.get(i) + ":" + name + ":" +status);
		}
	}
	
	/*
	 * function:生成不重复的随机数
	 * parameter:第一个参数maxNum为随机数范围的最大值，第二个参数amount为返回随机数的数量
	 * return:返回一个包含amount个随机数的数组，其中每个随机数的取值范围为：1 ~ maxNum，且所有随机数都不相等
	 */
	
	public static List<Integer> createRandomNumList (int maxNum,int amount){
		//存放当前生成的随机数的临时变量
		int tempNum = 0;
		List<Integer> randomNumList = new ArrayList<Integer>();
		while(randomNumList.size() < amount){
			tempNum = (int) Math.round(Math.random()*maxNum+0.5);
			//如果随机数数组中已包含刚生成的随机数，则刚生成的随机数丢弃
			if(!randomNumList.contains(tempNum)){
				randomNumList.add(tempNum);
			}
		}
		return randomNumList;
	}

}
