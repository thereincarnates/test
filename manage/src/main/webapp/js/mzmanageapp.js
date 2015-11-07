var manageapp=angular.module('mzmanageapp', ["ngRoute"]);
  
manageapp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'Home.html',
        controller: 'LoadManagePageCont'
      }).
      when('/home', {
        templateUrl: 'Home.html',
        controller: 'LoadManagePageCont'
      }).
      when('/manage/:optionid', {
        templateUrl: 'Home.html',
        controller: 'LoadManagePageCont'
      }).
     otherwise({
        redirectTo: '/home'
      });
  }]);

 manageapp.controller("LoadManagePageCont",function($scope,$routeParams,$http)
 { 
    $http({
          method: 'GET',
          url: '/manage/managemz',
          headers: {'Content-Type': 'application/json'}
        }).success(function (data) 
          {
            $scope.options=data;
            $scope.selectedItem = "Options";
            $scope.selFunction = function (item) {
               $scope.selectedItem = item;
               console.log(item);
               $http({
                        method: 'GET',
                        url: '/manage/managemz?optionid='+item,
                        headers: {'Content-Type': 'application/json'}
                    }).success(function (data)
                    {
                         $scope.dataTable = data;
                         var movieids = [];
                         var toggle = true;
                         $scope.onClick = function(row){
                            console.log(row.MovieId);
                            for(var key in movieids)
                            {
                               console.log("key")
                               if(!(movieids[key].localeCompare(row.MovieId)))
                               {
                                 console.log(key);
                                 movieids.splice(key,1);
                                 toggle = false;
                               }
                            }
                            if(toggle){
                              console.log("push");
                               movieids.push(row.MovieId);
                            }
                            else{
                               toggle = true;
                            }
                            
                         } 
                         $scope.sendData = function(){
                             console.log(item);
                             $http({
                                      method: 'POST',
                                      url: '/manage/managemz',
                                      headers: {'Content-Type': 'application/json'},
                                      params: {'movieId': movieids}
                                 });
                         }

                    });
               }
          });

 });

