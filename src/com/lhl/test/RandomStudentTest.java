package com.lhl.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lhl.db.DBUtil;

public class RandomStudentTest {
	
	public static void main(String[] args) throws Exception {
		Connection conn = DBUtil.getConnection();
	
		String sql = "SELECT student_id FROM student WHERE status = 'DONE'";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ResultSet rs = ptmt.executeQuery();
		List<Integer> excludedNumList = new ArrayList<Integer>();
		while(rs.next()){
			excludedNumList.add(rs.getInt("student_id"));
		}
		if(excludedNumList.size() > 37){
			System.out.println("剩余人数不足3人！");
			rs.close();
			ptmt.close();
			return;
		}
		rs.close();
		ptmt.close();
		
		//生成3个不等随机数，取值范围[1,40],且不包括excludedNumList中的任一数字
		List<Integer> randomNumList = createRandomNumList(40,3,excludedNumList);
		
		//修改数据库中这3个学生的status为'DONE'
		sql = "UPDATE student SET status = 'DONE' WHERE student_id in (?,?,?)";
		ptmt = conn.prepareStatement(sql);
		for(int i = 0; i < 3; i++){
			ptmt.setInt(i+1, randomNumList.get(i));
		}
		ptmt.executeUpdate();
		ptmt.close();
		
		//从数据库中查询这3个学生的name
		String name = null;
		String status = null;
		sql = "SELECT student_id, name, status FROM student WHERE student_id = ? ";
		ptmt = conn.prepareStatement(sql);
		for(int i = 0; i < 3; i++){
			ptmt.setInt(1, randomNumList.get(i));
			rs = ptmt.executeQuery();
			while(rs.next()){
				name = rs.getString("name");
				status = rs.getString("status");
			}
			System.out.println(randomNumList.get(i) + ":" + name + ":" + "go to work!" + status + "!"); 
		}
		rs.close();
		ptmt.close();
		conn.close();
	}
	
	/*
	 * function:生成不重复的随机数
	 * parameter:第一个参数maxNum为随机数范围的最大值，第二个参数amount为返回随机数的数量，第三个参数excludedNumList为已排除的数字集合
	 * return:返回一个包含amount个随机数的集合，其中每个随机数的取值范围为：1 ~ maxNum，且所有随机数都不相等，且所有随机数都不在excludedNumList中
	 */
	public static List<Integer> createRandomNumList (int maxNum,int amount,List<Integer> excludedNumList){
		//存放当前生成的随机数的临时变量
		int tempNum = 0;
		List<Integer> randomNumList = new ArrayList<Integer>();
		while(randomNumList.size() < amount){
			tempNum = (int) Math.round(Math.random()*maxNum+0.5);
			//如果 刚才生成的随机数(tempNum)包含于随机数集合(randomNumList) 或者 tempNum包含于excludedNumList中，则刚生成的随机数丢弃
			if(!randomNumList.contains(tempNum) && !excludedNumList.contains(tempNum) ){
				randomNumList.add(tempNum);
			}
		}
		return randomNumList;
	}

}
