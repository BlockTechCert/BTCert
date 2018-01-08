package org.bham.btcert.utils.merkle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MerkleTreeTest {
	
	@Test
	public void MerkleTest() {
		/*
		 * ArrayList<String> test = new ArrayList<String>();
		 * 
		 * MerkleTree m = new MerkleTree(test); m.addLeaf("1", true);
		 * m.addLeaf("2", true); m.makeTree();
		 * System.out.println(m.getMerkleRoot());
		 */

		/*
		 * Receipt receipt = new Receipt();
		 * receipt.setContext("https://w3id.org/chainpoint/v2");
		 * receipt.setType("ChainpointSHA256v2");
		 * receipt.setTargetHash("SSSSS"); receipt.setMerkleRoot("2222222");
		 */
		// receipt.setProof("test");

		/*
		 * List<HashMap<String,String>> proof = new
		 * ArrayList<HashMap<String,String>>(); HashMap<String, String> sibling
		 * = new HashMap<String,String>(); sibling.put("tttt", "tttt");
		 * 
		 * HashMap<String, String> sibling1 = new HashMap<String,String>();
		 * sibling1.put("rrrr", "rrrr");
		 * 
		 * proof.add(sibling); proof.add(sibling1); receipt.setProof(proof);
		 * 
		 * 
		 * List<Anchors> anchorsl = new ArrayList<Anchors>(); Anchors anchors =
		 * new Anchors(); anchors.setSourceId("txid");
		 * anchors.setType("BTCOpReturn"); anchorsl.add(anchors);
		 * receipt.setAnchors(anchorsl);
		 * 
		 * String jsonString = JSON.toJSONString(receipt);
		 * 
		 * System.out.println(jsonString);
		 * 
		 * String s = "[{\"tttt\":\"555\"},{\"rrrr\":\"rrrr\"}]";
		 * System.out.println(s); List<Map> list = JSON.parseObject(s,
		 * List.class); System.out.println((list.get(0)).get("tttt"));
		 */

		MerkleTree merkleTree = new MerkleTree();
		merkleTree.addLeaf("1", true);
		merkleTree.addLeaf("2", true);
		merkleTree.addLeaf("3", true);
		merkleTree.addLeaf("4", true);
		merkleTree.makeTree();
		merkleTree.makeReceipt(0);

		String proof = "[{\"right\":\"d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35\"},{\"right\":\"13656c83d841ea7de6ebf3a89e0038fea9526bd7f686f06f7a692343a8a32dca\"}]";
		boolean result = merkleTree.validateMerkleProof(proof,
				"6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b",
				"85df8945419d2b5038f7ac83ec1ec6b8267c40fdb3b1e56ff62f6676eb855e70");
		
		assertEquals(true,result);
		//System.out.println(result);
	}
	
}
