package org.bham.btcert.utils.merkle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bham.btcert.model.certificate.Anchors;
import org.bham.btcert.model.certificate.Signature;
import org.bham.btcert.utils.CryptoUtil;
import com.alibaba.fastjson.JSON;

/**
 * 
 * @Title: MerkleTree.java
 * @Package org.bham.btcert.utils.merkle
 * @Description: TODO MerkleTree
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
public class MerkleTree {

	//private static final String SHA_256 = "SHA-256";
	private ArrayList<String> leafs = new ArrayList<String>(); // 总叶子节点
	private List<ArrayList<String>> level = new ArrayList<ArrayList<String>>(); // 每个级别中的叶子数目

	/**
	 * 构造函数
	 * 
	 * @param tree
	 */
	public MerkleTree(ArrayList<String> leafs) {
		this.leafs = leafs;
	}

	public MerkleTree() {
	}

	/**
	 * 增加单个节点
	 * 
	 * @param data
	 * @param _hashLeafs
	 */
	public void addLeaf(String data, boolean _hashLeafs) {
		if (_hashLeafs == true) {
			data = CryptoUtil.hash(data).toHex();
		}
		leafs.add(data);
	}

	/**
	 * 批量增加节点
	 * 
	 * @param leafs
	 * @param _hashLeafs
	 */
	public void addLeaves(ArrayList<String> leafs, boolean _hashLeafs) {
		for (int i = 0; i < leafs.size(); i++) {
			addLeaf(leafs.get(i), _hashLeafs);
		}
	}

	/**
	 * 计算下一个层的节点个数
	 * 
	 * @param top_level
	 * @return
	 */
	public static ArrayList<String> calculateNextLevel(ArrayList<String> top_level) {

		ArrayList<String> result = new ArrayList<String>();
		int top_level_count = top_level.size();

		for (int i = 0; i < top_level_count; i = i + 2) {
			if (i <= top_level_count - 2) {
				String tohash = top_level.get(i) + top_level.get(i + 1);
				result.add(CryptoUtil.hash(tohash).toHex());
			} else {
				result.add(top_level.get(i));
			}
		}
		return result;
	}

	public void makeTree() {
		// 获取叶子总数
		int leaf_count = leafs.size();
		if (leaf_count > 0) {
			level.add(0, leafs); // 将全部节点放到第一层
			while (level.get(0).size() > 1) {
				level.add(0, calculateNextLevel(level.get(0))); // 将全部节点放到第一层
			}
		}
	}

	public String getMerkleRoot() {
		return level.get(0).get(0);
	}

	public String getLeaf(int index) {
		int leaf_level_index = level.size() - 1; // 高度
		if (index < 0 || index > (level.get(leaf_level_index).size() - 1)) {
			return null;
		}
		System.out.println("####" + level.get(leaf_level_index).get(index) + "####");
		return level.get(leaf_level_index).get(index);
	}

	/**
	 * getMerkleProof
	 * @param index
	 * @return
	 */
	public List<HashMap<String, String>> getMerkleProof(int index) {

		List<HashMap<String, String>> proof = new ArrayList<HashMap<String, String>>();

		int current_row_index = level.size() - 1; // get the row
		if (index < 0 || index > (level.get(current_row_index).size() - 1)) {
			return null;
		}
		
		for (int current_level = current_row_index; current_level > 0; current_level--) {
			int current_level_node_count = level.get(current_level).size(); // get current level
			if (index == current_level_node_count - 1 && current_level_node_count % 2 == 1) {
				index = (int) (Math.floor(index / 2)); 
				continue;
			}

			boolean is_right_node = (index % 2 == 1) ? true : false;
			int sibling_index = 0;
			String sibling_position = "";

			if (is_right_node) {
				sibling_index = index - 1;
				sibling_position = "left";
			} else {
				sibling_index = index + 1;
				sibling_position = "right";
			}
			String sibling_value = level.get(current_level).get(sibling_index);
			HashMap<String, String> sibling = new HashMap<String, String>();
			sibling.put(sibling_position, sibling_value);
			proof.add(sibling);
			index = (int) (Math.floor(index / 2));
		}
		return proof;
	}

	/**
	 * validateMerkleProof
	 * @param proof
	 * @param targetHash
	 * @param merkleRoot
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean validateMerkleProof(String proof, String targetHash, String merkleRoot) {
		if (proof == null || "".equals(proof)) {
			return targetHash.equals(merkleRoot);
		}

		String proofHash = targetHash;
		List<Map> list = JSON.parseObject(proof, List.class);

		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			if (map.containsKey("left")) {
				proofHash = CryptoUtil.hash(map.get("left") + proofHash).toHex();
			} else if (map.containsKey("right")) {
				proofHash = CryptoUtil.hash(proofHash + map.get("right")).toHex();
			} else {
				return false;
			}
		}
		
		return proofHash.equals(merkleRoot);
	}

	public String makeReceipt(int index) {

		Signature receipt = new Signature();
		receipt.setContext("https://w3id.org/chainpoint/v2");

		List<String> typelist = new ArrayList<String>();
		typelist.add("MerkleProof2017");
		typelist.add("Extension");
		receipt.setTypelist(typelist);

		receipt.setTargetHash(getLeaf(index));
		receipt.setMerkleRoot(getMerkleRoot());
		List<HashMap<String, String>> proof = getMerkleProof(index);
		receipt.setProof(proof);

		List<Anchors> anchorsl = new ArrayList<Anchors>();
		Anchors anchors = new Anchors();
		anchors.setSourceId("txid");
		anchors.setType("BTCOpReturn");
		anchorsl.add(anchors);
		receipt.setAnchors(anchorsl);

		String jsonString = JSON.toJSONString(receipt);
		return jsonString;
	}

	public Signature makeReceiptObject(int index) {

		Signature receipt = new Signature();
		receipt.setContext("https://w3id.org/chainpoint/v2");

		List<String> typelist = new ArrayList<String>();
		typelist.add("MerkleProof2017");
		typelist.add("Extension");
		receipt.setTypelist(typelist);

		receipt.setTargetHash(getLeaf(index));
		receipt.setMerkleRoot(getMerkleRoot());
		List<HashMap<String, String>> proof = getMerkleProof(index);
		receipt.setProof(proof);

		List<Anchors> anchorsl = new ArrayList<Anchors>();
		Anchors anchors = new Anchors();
		anchors.setSourceId("txid");
		anchors.setType("BTCOpReturn");
		anchorsl.add(anchors);
		receipt.setAnchors(anchorsl);

		// String jsonString = JSON.toJSONString(receipt);
		return receipt;
	}

	/**
	 * getLeafCount
	 * 
	 * @param leafs
	 * @param _hashLeafs
	 */
	public int getLeafCount(Map<String, ArrayList<byte[]>> tree) {
		ArrayList<byte[]> leaveslist = tree.get("leaves");
		return leaveslist.size();
	}

}
