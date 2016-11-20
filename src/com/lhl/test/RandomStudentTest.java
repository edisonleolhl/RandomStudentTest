package com.lhl.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lhl.db.JDBCUtils;

public class RandomStudentTest {

	public static void main(String[] args) throws Exception {

		List<Integer> excludedNumList = generateExcludedNumList();

		// 生成3个不等随机数，取值范围[1,40],且不包括excludedNumList中的任一数字
		List<Integer> randomNumList = generateRandomNumList(40, 3,
				excludedNumList);

		updateStudentStatus(randomNumList);

		printStudentInfo(randomNumList);
	}

	/**
	 * @param randomNumList
	 *            print student information whose student_id is in the
	 *            randomNumList
	 * @throws SQLException
	 */
	private static void printStudentInfo(List<Integer> randomNumList)
			throws SQLException {
		Connection conn = JDBCUtils.getConnection();
		String sql = "SELECT student_id, name, status FROM student WHERE student_id = ? ";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ResultSet rs = null;
		for (int i = 0; i < 3; i++) {
			ptmt.setInt(1, randomNumList.get(i));
			rs = ptmt.executeQuery();
			if (rs.next()) {
				System.out.println(randomNumList.get(i) + ":"
						+ rs.getString("name") + ":" + "go to work!"
						+ rs.getString("status") + "!");
			}
		}
		JDBCUtils.release(rs, ptmt, conn);
	}

	/**
	 * @param randomNumList
	 *            update student status whose student_id is in the randomNumList
	 * @throws SQLException
	 */
	private static void updateStudentStatus(List<Integer> randomNumList)
			throws SQLException {
		Connection conn = JDBCUtils.getConnection();
		String sql = "UPDATE student SET status = 'DONE' WHERE student_id in (?,?,?)";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		for (int i = 0; i < 3; i++) {
			ptmt.setInt(i + 1, randomNumList.get(i));
		}
		ptmt.executeUpdate();
		JDBCUtils.release(ptmt, conn);
	}

	/**
	 * @return the student_id list of which status is 'DONE'
	 * @throws SQLException
	 */
	private static List<Integer> generateExcludedNumList() throws SQLException {
		Connection conn = JDBCUtils.getConnection();
		String sql = "SELECT student_id FROM student WHERE status = 'DONE'";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ResultSet rs = ptmt.executeQuery();
		List<Integer> excludedNumList = new ArrayList<Integer>();
		if (excludedNumList.size() > 37) {
			System.out.println("剩余人数不足3人！");
			JDBCUtils.release(rs, ptmt, conn);
			return null;
		}
		while (rs.next()) {
			excludedNumList.add(rs.getInt("student_id"));
		}
		JDBCUtils.release(rs, ptmt, conn);
		return excludedNumList;
	}

	/**
	 * Generate non-repeated random number list. The random number range from 1
	 * to max.
	 * 
	 * @param max
	 *            the maximum of random number
	 * @param size
	 *            the size of the random number list
	 * @param excludedNumList
	 *            any number in this list should not be put into the return list
	 * @return a random number list
	 */
	public static List<Integer> generateRandomNumList(int max, int size,
			List<Integer> excludedNumList) {
		// 存放当前生成的随机数的临时变量
		int tempNum = 0;
		List<Integer> randomNumList = new ArrayList<Integer>();
		while (randomNumList.size() < size) {
			tempNum = (int) Math.round(Math.random() * max + 0.5);
			// 如果 刚才生成的随机数(tempNum)包含于随机数集合(randomNumList) 或者
			// tempNum包含于excludedNumList中，则刚生成的随机数丢弃
			if (!randomNumList.contains(tempNum)
					&& !excludedNumList.contains(tempNum)) {
				randomNumList.add(tempNum);
			}
		}
		return randomNumList;
	}

}
