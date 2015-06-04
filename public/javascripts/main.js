	
$(document).ready(function(){

	//词性分析类
	var wordAnalysis = function(){
		this.responseData = [];

		//由于不清楚可能含有的所有词性类型，
		//所以在前端写死，不能在后台判断后动态输出。
		this.mapData = {
			"t":"时间词",
			"w":"标点符号",
			"v":"动词",
			"u":"助词",
			"n":"名词",
			"p":"介词",
			"a":"形容词",
			"c":"连词",
			"d":"副词",
			"r":"代词",
			"m":"数词",
			"s":"处所词",
			"f":"方位词",
			"q":"量词",
			"y":"语气词"
		}
	}

	/*
	*****
	***** 事件处理函数
	*/	
	wordAnalysis.prototype.eventHandler = function(){
		var self = this;
		var submitBtnDom = $("#btn-analysis");
        EventUtil.addHandler(submitBtnDom,"click",function(event){
        	//鼠标点击事件，发送ajax请求
        	self.getResponseData();
        })  
	}

	/*
	*****
	***** ajax请求函数
	*/	
	wordAnalysis.prototype.getResponseData = function(){
	    var paramsName = null;
        var paramsValue = null;
        var listData = new DataBinder("/getBosNLP",paramsName,paramsValue,null,
                        this.sucCallback,this.errCallback,this.compCallback);
        listData.setAsync(true);
        listData.init();
        listData.requestData(function(){
            this.sucCallback(listData);
        }.bind(this));
        return listData;  
	}

	wordAnalysis.prototype.sucCallback = function(listData){
		this.responseData = listData.responseData;
	    //绘制文字的词性分析
    	this.drawWordAnalysis();
    	//绘制文字的词性类别解释
    	this.drawWordMean();
	}

	/*
	*****
	***** 绘制文字的词性分析
	*/
	wordAnalysis.prototype.drawWordAnalysis = function(){
		var str_tmp = "";
		var tag = this.responseData[0].tag;
		var word = this.responseData[0].word;

		for(var index in tag){
			tmp_tag = this.responseData[0].tag[index].substring(0,1);
			tmp_tag_type = this.mapData[tmp_tag];
			tmp_word = this.responseData[0].word[index];
			str_tmp += "<dd class=\""+tmp_tag+"\" title=\""
					+ tmp_tag_type+"\">"+tmp_word+"</dd>";
		}
		$(".words").html(str_tmp);
	}


	/*
	*****
	***** 绘制文字的词性类别解释
	*/
	wordAnalysis.prototype.drawWordMean = function(){
		var str_tmp = "<dt>词性类别图示:</dt>";

		for(var index in this.mapData){
			str_tmp += "<dd class=\""+index+"\">"
					+ this.mapData[index]+"</dd>";
		}
		$(".word-mean").html(str_tmp);
	}

	/*新建：词性分析对象*/
	var waObj = new wordAnalysis();
	/*注册：词性分析对象事件处理函数*/
	waObj.eventHandler();

});







