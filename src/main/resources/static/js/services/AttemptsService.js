appServices.factory('AttemptService', function($log, $resource) {

	var attService = {};
	
	return {
		getAll: function () {
	        var attResource = $resource('attempts?', {}, {
	            query: {method: 'GET', params: {}, isArray: true}
	        });
	        //console.log('==============AttemptService==============');
	        return attResource.query();
	    },
	    getAllByGWID: function (gwId) {
	        var attResource = $resource('attemptsGW?gwId='+gwId, {}, {
	            query: {
	            		method: 'GET', 
	            		params: {}, 
	            		isArray: true,
		            	transformResponse: function (data) {
		            		
		            		var result = angular.fromJson(data);
		            		//console.log('result in getAllByGWID', result);
		            		angular.forEach(result, function(r) {
                    			//console.log('r', r);
                    			if (r.attemptResponseDetail == null)
                    				r.attemptResponseDetail = 'OK';
                            });
		            		//console.log('result', result);
		            		return result;
		            	}
	            }
	        });
	        //console.log('==============AttemptService with GWID==============');
	        return attResource.query();
	    }
	}

	return attService;
});

