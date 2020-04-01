appServices.factory('QueueService', function($log, $resource) {

	var queueService = {};
	
	return {
		getAll: function () {
			//console.log('entro in getAll');
	        var summaryResource = $resource('queue/summary?queueName=*', {}, {
	            query: {
	            	method: 'GET', 
	            	params: {}, 
	            	isArray: true,
	            	transformResponse: function (data) {
	            		
	            		//console.log('In Summary RESOURCE!!!!');
	            		
	            		var result = angular.fromJson(data);
	            		//console.log('result', result);
	            		
	        			angular.forEach(result, function(queue, key) {
	                    	var queuKV = key.split('.');
	                    	if (queuKV[0] == 'yucca_light'){
	
	                    		var tmpQueue = null;
	                    		var queueName = 'yucca_light.' + queuKV[1];
	                    		tmpQueue = result[queueName];
	                    		
	                    		angular.forEach(workflowStateObj, function(wf) {
	                    			if (wf.name == queueName){
	                    				wf.countMessages = tmpQueue.countMessages;
	                    				wf.lastmessage = tmpQueue.lastmessage;
	                    			}
	                            });
	                    	}
	        			});
	            	}
	            }
	        });
	        summaryResource.query();
	      //console.log('workflowStateObj', workflowStateObj);
	        return workflowStateObj;
		},
		
		getMessagesOfQueue: function (qn) {

			//console.log('queueName => ', qn);
			var queue = qn.split('.');
	        var queueResource = $resource('queue?queueName=:nameQueue', {nameQueue:queue[1]}, {
	            query: {
	            	method: 'GET', 
	            	params: {}, 
	            	isArray: true,
	            	transformResponse: function (data) {
	            		
	            		var result = angular.fromJson(data);
	            		
	            		angular.forEach(result, function(queue, key) {
	            			//console.log('queue', queue);
	                    	var queuKV = key.split('.');
	                    	if (queuKV[0] == 'yucca_light'){
	                    		angular.forEach(workflowStateObj, function(wf) {
	                    			if (wf.name == key){
	                    				wf.messages = queue;
	                    			}
	                    		});
	                    	}
                        });
	            	}
	            }
	        });
	        queueResource.query();
	      //console.log('workflowStateObj', workflowStateObj);
	        return true;
	    },
	    
	    moveQueue: function(qS, gwId, qD){
	    	//console.log('moveQueue', 'OK!!');
	    	var rtn = false;
	    	var queueResource = $resource('queue/move?queueSource=:queueS&gwId=:gwId&queueDestination=:queueD', {
	    		queueS:qS, gwId:gwId, queueD:qD}, {
	            query: {
	            	method: 'GET', 
	            	params: {}, 
	            	isArray: false,
	            	transformResponse: function (data) {
	            		//console.log('data', data);
	            	}
	            }
	    	});

	        queueResource.query();
	    	return rtn;
	    }
    };
    
});

