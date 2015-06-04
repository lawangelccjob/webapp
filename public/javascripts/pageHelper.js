

/**********
URL类
*************/   
    window.Url = function(urlHref) {
        this.urlHref = urlHref;
        this.paramsName = null;
        this.paramsValue = null;
    }

    Url.prototype.getUrlHref = function(){
        return this.urlHref;
    }

    Url.prototype.setUrlHref =function(urlHref){
        this.urlHref = urlHref;
    }

    Url.prototype.setParameterName =function(paramsName){
        this.paramsName = paramsName;
    }

    Url.prototype.getParameterName =function(){
        return this.paramsName;
    }

    Url.prototype.setParameterValue =function(paramsValue){
        this.paramsValue = paramsValue;
    }

    Url.prototype.getParameterValue =function(){
        return this.paramsValue;
    }

    Url.prototype.getCurUrlHref = function(){
        return window.location.href;
    }
    
    Url.prototype.getUrl = function(){
        var paraName = this.getParameterName(),
            paraItem = this.getParameterValue(),
            tempurl=this.getUrlHref();  
        for(var index in paraName){
            var predot = (0 == index)?"?":"&";
            tempurl += predot + paraName[index]+"="+paraItem[index];                
        }
        return tempurl;
    }

/**********
事件处理类
*************/
    var EventUtil = {

        addHandler: function(element, type, handler){
            $(element.selector).bind(type,handler);
        },
        removeHandler:function(element, type, handler){
            $(element.selector).unbind(type);
        },
        getEvent:function(event){
            return event ? event : window.event;
        },
        getTarget: function(event){
            return event.target || event.srcElement;
        },

        addKeydownEvent: function(element,type,handler){
          $(element.selector).keydown(handler);
        },

        preventDefault: function(event){
            if(event.preventDefault){
                event.preventDefault();
            }else{
                event.returnValue = false;
            }
        },
        stopPropagation: function(event){
            if(event.stopPropagation){
                event.stopPropagation();
            }else{
                event.cancelBubble = true;
            }
        }
    };

/**********
数据绑定类
*************/

    var DataBinder = function(urlHref,paramsName,paramsValue,bfsendCallback,sucCallback,errCallback,compCallback){

        this.responseData = null;
        this.successData = null
        this.errormsg = null;
        this.handerData = null;
        this.url = null;
        this.urlHref = urlHref;
        this.paramsName = paramsName;
        this.paramsValue = paramsValue;
        this.bfsendCallback = bfsendCallback;
        this.sucCallback = sucCallback;
        this.errCallback = errCallback;
        this.compCallback = compCallback;       
        this.ajaxtype = 'get';
        this.async = false;
    }

    DataBinder.prototype.init = function(){
        this.url = new Url(this.urlHref);
        this.url.setParameterName(this.paramsName);
        this.url.setParameterValue(this.paramsValue);
    }

    DataBinder.prototype.setAjaxType = function(type){
        this.ajaxtype = type;
    }

    DataBinder.prototype.setAsync = function(async){
        this.async = async;
    }   

    DataBinder.prototype.requestData = function(sucCallback,compCallback){
        var that = this;
        var htmlobj=$.ajax({
            url:that.url.getUrl(),
            async:this.async,                
            dataType:"json",  
            type:this.ajaxtype,    
            beforeSend:function(){
                that.bfsendCallback;
            },
            success:function(data){  
                that.responseData = data;
                that.handerData = sucCallback;
                if('function' == typeof(that.handerData)){
                    that.handerData(data);
                }
            },
            error:function(data){
                that.responseData = data;
                that.errormsg = that.errCallback;
                if('function' == typeof(that.handerData)){
                    that.errormsg();
                }        
            },
            complete:function(data){
                that.responseData = data;
                that.handerData = compCallback;  
                if('function' == typeof(that.handerData)){
                    that.handerData(data);
                }                
            }
        });          
    }

    DataBinder.prototype.getResponseData = function(){
        return this.responseData;
    }

    DataBinder.prototype.getHanderData = function(){
        return this.handerData;
    }





