function UserController($scope, $http)
{
  $scope.user = {};
 
  $scope.createUser = function() 
  {
    $http({
      method: 'POST',
      url: 'http://127.0.0.1:8080/ui/user',
      headers: {'Content-Type': 'application/json'},
      data:  $scope.user
    }).success(function (data) 
      {
    	$scope.status=data;
      });
  };
}