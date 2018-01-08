/**
 * @author rxl635@student.bahm.ac.uk
 * btcert.is is used for handle the bitcoin blockchain protocol.
 * @reference https://coinb.in/
 */


(function () {
	
	var btcert = window.btcert = function () { };
	
	//configure
	btcert.api = "";
	btcert.redeem = {'addr':'3DWwmE19gHdv3xM3zPrVwqoRmbJQ6cCPHd', 'type':'multiaddrss', 'script':'5221038b1df5a4e05a0ee0a36b5d3a1e44c22ebdb2a0ce57df45650b06b167a85d48d4210379ed77927da05af3b854248c0a6b15e10b41437307c1f6e099f755fc0b973ae321031cf51d3076172465633b6faca2499da938b1c99832922533bcbfc1cf9c40884153ae'};
	btcert.tranction = {};
	btcert._tranction_inout = [];
	btcert._tranction_output = [];
	//tranctionFee
	btcert._tranction_tranctionFee = 0.0001*1000*1000*100
	btcert._tranction_recyleValue = 0.002*1000*1000*100
	btcert._tranction_totalInputValue = 0
	btcert._tranction_lock_time = 0
	btcert._tranction_version = 1
	btcert._tranction_signatures = [];
	btcert._tranction_version_multisig = 0x05;
	
	// get the UnspentTransaction from the bitcoin transaction.
	btcert.getUnspentTransaction = function(callback){
		
		var redeem = this.redeem;
		$.ajax ({
			type: "GET",
			url: "https://chain.so/api/v2/get_tx_unspent/BTC/"+redeem.addr+"?unconfirmed=1",
			dataType: "json",
			error: function(data) {
			},
			success: function(data) {
				if((data.status && data.data) && data.status=='success'){
					btcert._unspentTransaction_flag = true;
					for(var i in data.data.txs){
						var o = data.data.txs[i];
						var tx = o.txid;
						var n = o.output_no;
						var script =(redeem.type =='multiaddrss') ? redeem.script : o.script_hex;
						//var script = o.script;
						var amount = o.value;
						btcert.initInput(tx, n, script, 4294967295,amount);
					}
					callback();
				} else {
					
				}
			},
			complete: function(data, status) {
				// do nothing
			}
		});
	}
	
	// broadcast the raw transaction
	btcert.broadcastRawtransaction = function(raw_tran,succesCallback,errCallback){
        $.ajax ({
			type: "POST",
			url: "https://chain.so/api/v2/send_tx/BTC/",
			data: {"tx_hex":raw_tran},
			dataType: "json",
			error: function(data) {
				errCallback(data);
			},
            success: function(data) {
				if(data.status && data.data.txid){
					succesCallback(data);
				} else {
					errCallback(data);
				}				
			},
			complete: function(data, status) {
							
			}
		});
	}
	
	
	// initInput
	btcert.initInput = function(hash,index,scriptBytes,sequence,inputValue){
		var txin = {hash:hash,index:index,scripts:scriptBytes,scriptBytes:Crypto.util.hexToBytes(scriptBytes),sequence:sequence}
		this._tranction_inout.push(txin);
		this._tranction_totalInputValue = this._tranction_totalInputValue + inputValue*1000*1000*100;
		alert(this._tranction_totalInputValue)
	}
	
	//initOutPut
	btcert.initOutput = function(intputScript,value){
		
		//whether it is number.
		if(((intputScript.match(/^[a-f0-9]+$/gi)) && intputScript.length<160) && (intputScript.length%2)==0) {
			var txop_data = {scriptHex:Crypto.util.bytesToHex(this.getOutScriptData(intputScript)),scriptBytes:this.getOutScriptData(intputScript),value: 0};
			this._tranction_output.push(txop_data);
			return;
		}
		
		//recyleValue = 0.005*1000*1000*100;
		recyleValue = btcert._tranction_recyleValue;
		mainValue = this._tranction_totalInputValue - recyleValue - this._tranction_tranctionFee
		
		var addrsess = btcert.base58decode(intputScript);
		var version = addrsess[0];
		if(version == 0x05){ //multi-signature
			var txop_main = {scriptHex:Crypto.util.bytesToHex(this.getOutScriptMultiAddrss(intputScript)),scriptBytes:this.getOutScriptMultiAddrss(intputScript),value:mainValue}
			this._tranction_output.push(txop_main);
		}else { //pubk
			var txop_recycle = {scriptHex:Crypto.util.bytesToHex(this.getOutScriptAddrss(intputScript)),scriptBytes:this.getOutScriptAddrss(intputScript),value:recyleValue}
			this._tranction_output.push(txop_recycle);
		}
	}
	
	//serialize a transaction
	btcert.serialize = function(){
		var buffer = [];
		buffer = buffer.concat(btcert.numToBytes(parseInt(this._tranction_version),4));
		buffer = buffer.concat(btcert.numToVarInt(this._tranction_inout.length));
		
		for (var i = 0; i < this._tranction_inout.length; i++) {
			var txin = this._tranction_inout[i];
			buffer = buffer.concat(Crypto.util.hexToBytes(txin.hash).reverse());
			buffer = buffer.concat(btcert.numToBytes(parseInt(txin.index),4));
			var scriptBytes = txin.scriptBytes;
			buffer = buffer.concat(btcert.numToVarInt(scriptBytes.length));
			buffer = buffer.concat(scriptBytes);
			buffer = buffer.concat(btcert.numToBytes(parseInt(txin.sequence),4));
		}
		
		buffer = buffer.concat(btcert.numToVarInt(this._tranction_output.length));
		

		for (var i = 0; i < this._tranction_output.length; i++) {
			var txout = this._tranction_output[i];
				buffer = buffer.concat(btcert.numToBytes(txout.value,8));
			var scriptBytes = txout.scriptBytes;
			buffer = buffer.concat(btcert.numToVarInt(scriptBytes.length));
			buffer = buffer.concat(scriptBytes);
		}
		
		buffer = buffer.concat(btcert.numToBytes(parseInt(this._tranction_lock_time),4));
		
		return Crypto.util.bytesToHex(buffer);
	}
	
	// reverse the transactionId
	btcert.reverse = function(str){
		 var buffer = Crypto.util.hexToBytes(str);
		 var str = Crypto.util.bytesToHex(Crypto.SHA256(Crypto.SHA256(buffer, {asBytes: true}), {asBytes: true}));
		
		 var re = [];
		 for (var i = str.length -1; i >=0; i = i -2) 
		 { 
			 re.push(str[i-1]);
			 re.push(str[i]);
		 }
		 txid = re.join("");
		 return txid
	}
	
	//get the get Transaction Hash
	btcert.getTransactionHash = function(index){
		var clone = btcert.clone(btcert);
		
		for (var i = 0; i < clone._tranction_inout.length; i++) {
			if(index != i){
				clone._tranction_inout[i].scriptBytes = [];
			}
		}
		//clone._tranction_inout[index].scriptBytes = Crypto.util.hexToBytes(btcert._tranction_inout[index].scripts);
		clone._tranction_inout[index].scriptBytes = Crypto.util.hexToBytes(btcert.redeem.script);
		var serTransaction = btcert.serialize();
		
		var buffer = Crypto.util.hexToBytes(serTransaction);
		buffer = buffer.concat(btcert.numToBytes(parseInt(1), 4));
		var hash = Crypto.SHA256(buffer, {asBytes: true});
		var r = Crypto.util.bytesToHex(Crypto.SHA256(hash, {asBytes: true}));
		return r;
	}
	
	
	// clone an object
	btcert.clone = function(obj) {
		if(obj == null || typeof(obj) != 'object') return obj;
		var temp = new obj.constructor();

		for(var key in obj) {
			if(obj.hasOwnProperty(key)) {
				temp[key] = coinjs.clone(obj[key]);
			}
		}
 		return temp;
	}
	
	
	// get the hash signature
	// @reference: https://coinb.in/js/coin.js
	btcert.getHashSignature = function(hash,wif){
		
		function serializeSig(r, s) {
			var rBa = r.toByteArraySigned();
			var sBa = s.toByteArraySigned();

			var sequence = [];
			sequence.push(0x02); // INTEGER
			sequence.push(rBa.length);
			sequence = sequence.concat(rBa);

			sequence.push(0x02); // INTEGER
			sequence.push(sBa.length);
			sequence = sequence.concat(sBa);

			sequence.unshift(sequence.length);
			sequence.unshift(0x30); // SEQUENCE

			return sequence;
		}
		hash = Crypto.util.hexToBytes(hash);
		var curve = EllipticCurve.getSECCurveByName("secp256k1");
		var key = btcert.wif2privkey(wif);
		var priv = BigInteger.fromByteArrayUnsigned(Crypto.util.hexToBytes(key['privkey']));
		var n = curve.getN();
		var e = BigInteger.fromByteArrayUnsigned(hash);
		var badrs = 0
		do {
			var k = this.deterministicK(wif, hash, badrs);
			var G = curve.getG();
			var Q = G.multiply(k);
			var r = Q.getX().toBigInteger().mod(n);
			var s = k.modInverse(n).multiply(e.add(priv.multiply(r))).mod(n);
			badrs++
		} while (r.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(BigInteger.ZERO) <= 0);
		
		var halfn = n.shiftRight(1);
		if (s.compareTo(halfn) > 0) {
			s = n.subtract(s);
		};

		var sig = serializeSig(r, s);
		sig.push(parseInt(1, 10));
		return Crypto.util.bytesToHex(sig);
	}
	
	// convert a wif key back to a private key
	// @reference: https://coinb.in/js/coin.js
	btcert.wif2privkey = function(wif){
		var compressed = false;
		var decode = btcert.base58decode(wif);
		var key = decode.slice(0, decode.length-4);
		key = key.slice(1, key.length);
		if(key.length>=33 && key[key.length-1]==0x01){
			key = key.slice(0, key.length-1);
			compressed = true;
		}
		return {'privkey': Crypto.util.bytesToHex(key), 'compressed':compressed};
	}
	
	// redeemScript to pubkeys
	btcert.redeemScript2pubkeys = function(script){
		
		var scriptbyte = Crypto.util.hexToBytes(script);
		var signaturesRequired = scriptbyte[0] -80;
		var pubkeyNums = scriptbyte[scriptbyte.length - 2] -80;
		
		var pubkeys = [];
		var opcodes = [];
		
		scriptbyte = scriptbyte.slice(1,scriptbyte.length - 2);
		
		for(var i = 0 ; i < pubkeyNums; i++){
			var pk = scriptbyte.slice(i*33+(i+1), (i + 1)*33+(i+1));
			pubkeys.push(Crypto.util.bytesToHex(pk));
		}
		
		return {'signaturesRequired':signaturesRequired,'pubkeyNums':pubkeyNums,'pubkeys':pubkeys}
	}
	
	//  pubkeys to redeemScript
	btcert.pubkeys2MultisigAddress = function(pubkeys, required) {
		var multisigAddress = [];
		multisigAddress.push(81 + (required*1) - 1); //OP_1
		for (var i = 0; i < pubkeys.length; i++) {
			var pubkeybyte = Crypto.util.hexToBytes(pubkeys[i]);
			multisigAddress.push(pubkeybyte.length)
			multisigAddress = multisigAddress.concat(pubkeybyte);
		}
		multisigAddress.push(81 + pubkeys.length - 1)
		multisigAddress.push(174) //OP_CHECKMULTISIG
		var x = ripemd160(Crypto.SHA256(multisigAddress, {asBytes: true}), {asBytes: true});
		x.unshift("0x05");
		var r = x;
		r = Crypto.SHA256(Crypto.SHA256(r, {asBytes: true}), {asBytes: true});
		var checksum = r.slice(0,4);
		var redeemScript = Crypto.util.bytesToHex(multisigAddress);
		var address = btcert.base58encode(x.concat(checksum));
		return {'address':address, 'redeemScript':redeemScript};
	}
	
	// base58 encode function
	// @reference: https://coinb.in/js/coin.js
	btcert.base58encode = function(buffer) {
		var alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
		var base = BigInteger.valueOf(58);

		var bi = BigInteger.fromByteArrayUnsigned(buffer);
		var chars = [];

		while (bi.compareTo(base) >= 0) {
			var mod = bi.mod(base);
			chars.unshift(alphabet[mod.intValue()]);
			bi = bi.subtract(mod).divide(base);
		}

		chars.unshift(alphabet[bi.intValue()]);
		for (var i = 0; i < buffer.length; i++) {
			if (buffer[i] == 0x00) {
				chars.unshift(alphabet[0]);
			} else break;
		}
		return chars.join('');
	}
	
	// https://tools.ietf.org/html/rfc6979#section-3.2
	btcert.deterministicK = function(wif, hash, badrs) {
		// if r or s were invalid when this function was used in signing,
		// we do not want to actually compute r, s here for efficiency, so,
		// we can increment badrs. explained at end of RFC 6979 section 3.2

		// wif is b58check encoded wif privkey.
		// hash is byte array of transaction digest.
		// badrs is used only if the k resulted in bad r or s.

		// some necessary things out of the way for clarity.
		badrs = badrs || 0;
		var key = btcert.wif2privkey(wif);
		var x = Crypto.util.hexToBytes(key['privkey'])
		var curve = EllipticCurve.getSECCurveByName("secp256k1");
		var N = curve.getN();

		// Step: a
		// hash is a byteArray of the message digest. so h1 == hash in our case

		// Step: b
		var v = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];

		// Step: c
		var k = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

		// Step: d
		k = Crypto.HMAC(Crypto.SHA256, v.concat([0]).concat(x).concat(hash), k, { asBytes: true });

		// Step: e
		v = Crypto.HMAC(Crypto.SHA256, v, k, { asBytes: true });

		// Step: f
		k = Crypto.HMAC(Crypto.SHA256, v.concat([1]).concat(x).concat(hash), k, { asBytes: true });

		// Step: g
		v = Crypto.HMAC(Crypto.SHA256, v, k, { asBytes: true });

		// Step: h1
		var T = [];

		// Step: h2 (since we know tlen = qlen, just copy v to T.)
		v = Crypto.HMAC(Crypto.SHA256, v, k, { asBytes: true });
		T = v;

		// Step: h3
		var KBigInt = BigInteger.fromByteArrayUnsigned(T);

		// loop if KBigInt is not in the range of [1, N-1] or if badrs needs incrementing.
		var i = 0
		while (KBigInt.compareTo(N) >= 0 || KBigInt.compareTo(BigInteger.ZERO) <= 0 || i < badrs) {
			k = Crypto.HMAC(Crypto.SHA256, v.concat([0]), k, { asBytes: true });
			v = Crypto.HMAC(Crypto.SHA256, v, k, { asBytes: true });
			v = Crypto.HMAC(Crypto.SHA256, v, k, { asBytes: true });
			T = v;
			KBigInt = BigInteger.fromByteArrayUnsigned(T);
			i++
		};

		return KBigInt;
	};
	
	
	btcert.getOutScriptMultiAddrss = function(addr){
		
		var bytes = btcert.base58decode(addr);
		var newBytes = bytes.slice(1, bytes.length-4);
		
		var scriptByte = [];
		scriptByte.push(169);
		scriptByte.push(newBytes.length);
		scriptByte = scriptByte.concat(newBytes)
		scriptByte.push(135);
		
		return scriptByte;
	}

	btcert.getOutScriptAddrss = function(addr){
		
		var bytes = btcert.base58decode(addr);
		var newBytes = bytes.slice(1, bytes.length-4);
		
		var scriptByte = [];
		
		scriptByte.push(118);
		scriptByte.push(169);
		
		scriptByte.push(newBytes.length);
		
		scriptByte = scriptByte.concat(newBytes)
		
		scriptByte.push(136);
		scriptByte.push(172);
		
		return scriptByte;
	}
	
	btcert.getOutScriptData = function(data){
		var scriptByte = [];
		scriptByte.push(106)
		scriptByte.push(Crypto.util.hexToBytes(data).length)
		//scriptByte.push(Crypto.util.hexToBytes(data))
		scriptByte = scriptByte.concat(Crypto.util.hexToBytes(data))
		return scriptByte;
	}
	
	btcert.numToBytes = function(num,bytes) {
		if (typeof bytes === "undefined") bytes = 8;
		if (bytes == 0) { 
			return [];
		} else if (num == -1){
			return Crypto.util.hexToBytes("ffffffffffffffff");
		} else {
			return [num % 256].concat(btcert.numToBytes(Math.floor(num / 256),bytes-1));
		}
	}
	
	
	btcert.numToByteArray = function(num) {
		if (num <= 256) { 
			return [num];
		} else {
			return [num % 256].concat(btcert.numToByteArray(Math.floor(num / 256)));
		}
	}

	btcert.numToVarInt = function(num) {
		if (num < 253) {
			return [num];
		} else if (num < 65536) {
			return [253].concat(btcert.numToBytes(num,2));
		} else if (num < 4294967296) {
			return [254].concat(btcert.numToBytes(num,4));
		} else {
			return [255].concat(btcert.numToBytes(num,8));
		}
	}

	btcert.bytesToNum = function(bytes) {
		if (bytes.length == 0) return 0;
		else return bytes[0] + 256 * btcert.bytesToNum(bytes.slice(1));
	}
	
	
	btcert.base58decode = function(buffer){
		var alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
		var base = BigInteger.valueOf(58);
		var validRegex = /^[1-9A-HJ-NP-Za-km-z]+$/;

		var bi = BigInteger.valueOf(0);
		var leadingZerosNum = 0;
		for (var i = buffer.length - 1; i >= 0; i--) {
			var alphaIndex = alphabet.indexOf(buffer[i]);
			if (alphaIndex < 0) {
				throw "Invalid character";
			}
			bi = bi.add(BigInteger.valueOf(alphaIndex).multiply(base.pow(buffer.length - 1 - i)));

			if (buffer[i] == "1") leadingZerosNum++;
			else leadingZerosNum = 0;
		}

		var bytes = bi.toByteArrayUnsigned();
		while (leadingZerosNum-- > 0) bytes.unshift(0);
		return bytes;		
	}
	
	/* convert a wif key back to a private key */
	btcert.wif2privkey = function(wif){
		var compressed = false;
		var decode = btcert.base58decode(wif);
		var key = decode.slice(0, decode.length-4);
		key = key.slice(1, key.length);
		if(key.length>=33 && key[key.length-1]==0x01){
			key = key.slice(0, key.length-1);
			compressed = true;
		}
		return {'privkey': Crypto.util.bytesToHex(key), 'compressed':compressed};
	}

	/* convert a wif to a pubkey */
	btcert.wif2pubkey = function(wif){
		var compressed = btcert.compressed;
		var r = btcert.wif2privkey(wif);
		btcert.compressed = r['compressed'];
		var pubkey = btcert.newPubkey(r['privkey']);
		btcert.compressed = compressed;
		return {'pubkey':pubkey,'compressed':r['compressed']};
	}

	/* convert a wif to a address */
	btcert.wif2address = function(wif){
		var r = btcert.wif2pubkey(wif);
		return {'address':btcert.pubkey2address(r['pubkey']), 'compressed':r['compressed']};
	}
	
	/* generate a public key from a private key */
	btcert.newPubkey = function(hash){
		var privateKeyBigInt = BigInteger.fromByteArrayUnsigned(Crypto.util.hexToBytes(hash));
		var curve = EllipticCurve.getSECCurveByName("secp256k1");

		var curvePt = curve.getG().multiply(privateKeyBigInt);
		var x = curvePt.getX().toBigInteger();
		var y = curvePt.getY().toBigInteger();

		var publicKeyBytes = EllipticCurve.integerToBytes(x, 32);
		publicKeyBytes = publicKeyBytes.concat(EllipticCurve.integerToBytes(y,32));
		publicKeyBytes.unshift(0x04);

		if(btcert.compressed==true){
			var publicKeyBytesCompressed = EllipticCurve.integerToBytes(x,32)
			if (y.isEven()){
				publicKeyBytesCompressed.unshift(0x02)
			} else {
				publicKeyBytesCompressed.unshift(0x03)
			}
			return Crypto.util.bytesToHex(publicKeyBytesCompressed);
		} else {
			return Crypto.util.bytesToHex(publicKeyBytes);
		}
	}
	
	
})()
