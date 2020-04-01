appControllers.controller('QueueController', [ 
        '$scope', 
        '$modal', 
        '$log', 
        'QueueService', 
        'AttemptService',
        function($scope, $modal, $log, QueueService, AttemptsService) {
        	
        	$scope.intervalToken = null;

			$scope.getAllQueues = function() {
				//console.log('Entro in getAllQueues', $scope.queueList);
				$scope.queueList = [];
				//console.log('Sono in getAllQueues', $scope.queueList);
	        	$scope.queueList = QueueService.getAll();
	        	//console.log('Esco da getAllQueues', $scope.queueList);
			};
			
        	$scope.getAllQueues();
        	
        	//console.log('queueList', $scope.queueList);
        	
        	$scope.resendrt = $scope.queueList.s0_to_send_rt.name;
        	$scope.resenda2a = $scope.queueList.s4_to_send_a2a.name;
        	
			$scope.filteredQueuesList = [];
			$scope.codeFilter = null;
			$scope.currentPage = 1;
			$scope.pageSize = 10;
			$scope.totalItems = $scope.queueList.length;
			$scope.predicate = '';
			$scope.showLoading = false;

			$scope.searchCodeFilter = function(queue) {
				var keyword = new RegExp($scope.codeFilter, 'i');
				return !$scope.codeFilter || keyword.test(queue.streamCode) || keyword.test(queue.sourceCode);
			};

			$scope.$watch('codeFilter', function(newCode) {
				$scope.currentPage = 1;
				$scope.totalItems = $scope.filteredQueuesList.length;
			});
			
			$scope.loadMessagesOfQueue = function(queueName){
				QueueService.getMessagesOfQueue(queueName);
			}
			
			$scope.loadAttempts = function(nameQueue, gwId){
				$scope.stopRefresh();
				var result = AttemptsService.getAllByGWID(gwId);
				//console.log('result', result);

        		angular.forEach($scope.queueList, function(quVal, quKey) {
        			var keyVal = quKey.substr(3);
        			if ('yucca_light.'+keyVal == nameQueue){
        				angular.forEach(quVal.messages, function(msgVal, msgKey) {
        					if (msgVal.headers.gwId ==  gwId){
        						msgVal.attemps = {};
        						msgVal.attemps = result;
        					}
        				});
        			}
        		});

        		//console.log('queueList', $scope.queueList);
			}
			
			$scope.moveToQueue = function(queueSource, gwId, queueDestination){
				//console.log('queueSource', queueSource);
				//console.log('gwId', gwId);
				//console.log('queueDestination', queueDestination);
				
				var queueS = queueSource.split('.');
				var queueD = queueDestination.split('.');
				
				var rtn = QueueService.moveQueue(queueS[1], gwId, queueD[1]);
				$scope.startRefresh();
			}
			
			$scope.open = function(startQueue, gwId){

			    var modalInstance = $modal.open({
			      animation: $scope.animationsEnabled,
			      templateUrl: 'myModalContent.html',
			      controller: 'ModalInstanceCtrl',
			      resolve: {
			        items: function() {
			        	var rtnArr = [startQueue, gwId];
			          return rtnArr;
			        },
			        queueList: function(){
			        	return $scope.queueList;
			        }
			      }
			    });

			    modalInstance.result.then(function (selectedItem) {
			      $scope.selected = selectedItem;
			    }, function () {
			      $log.info('Modal dismissed at: ' + new Date());
			    });
			  };
			  
			  $scope.refresh = function(){
				  $scope.getAllQueues();
				  $scope.loadMessagesOfQueue(workflowStateObj.s0_to_send_rt.name);
				  $scope.loadMessagesOfQueue(workflowStateObj.s1_sending_rt_progress.name);
				  $scope.loadMessagesOfQueue(workflowStateObj.s2_sent_rt.name);
				  $scope.loadMessagesOfQueue(workflowStateObj.s3_sending_failed.name);
				  $scope.loadMessagesOfQueue(workflowStateObj.s4_to_send_a2a.name);
				  $scope.loadMessagesOfQueue(workflowStateObj.s5_sending_a2a_progress.name);
				  $scope.loadMessagesOfQueue(workflowStateObj.s6_sent_a2a.name);
				  $scope.loadMessagesOfQueue(workflowStateObj.s7_sent_invalid.name);
				  $scope.getAllQueues();
			  };
			  
			  $scope.startRefresh = function() {
				  if ($scope.intervalToken == null)
					  $scope.intervalToken = setInterval(function(){
						  $scope.refresh();
					  }, 1000);
			  }
			  
			  $scope.stopRefresh = function() {
				  clearInterval($scope.intervalToken);
				  $scope.intervalToken = null;
			  }
			  
			  $scope.startRefresh();
		} 
]);

appControllers.controller('ModalInstanceCtrl', function ($scope, $modalInstance, items, queueList) {

	$scope.items = items;
	$scope.queueList = queueList; 
	
	$scope.radioModel = '';

	$scope.ok = function () {
		//$modalInstance.close($scope.selected.item);
		//console.log('formData.chickenEgg', formData.chickenEgg);
		//console.log('scope.radioModel', $scope.radioModel);
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
});
