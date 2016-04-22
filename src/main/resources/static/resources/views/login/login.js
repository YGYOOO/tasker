tmod = angular.module('Things.Login', ['ngRoute']);

tmod.config(['$routeProvider', function($routeProvider) {
   var config = {
    templateUrl: '/resources/views/login/login.html',
    controller: 'Things.Login.Controller'
   };
   
   $routeProvider.when('/', config).when( '/login', config );
}]);

tmod.controller('Things.Login.Controller', ['$scope', '$http', '$location', 'Session.Service', function($scope, $http, $location, Session) {
   $scope.username = '';
   $scope.password = '';
   $scope.hasError = false;

   $scope.isLoggedIn = Session.isLoggedIn;

   if( Session.isLoggedIn() ) {
      $location.path('/players');
   }
   
   $scope.login = function() {
      var req = {
         method : 'POST',
         url : '/login',
         headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
         },
         data : 'username=' + $scope.username + '&password=' + $scope.password
      };

      $http( req ).then(
         function( response ) {
            $scope.hasError = false;
            Session.create( response.data );
            $location.url($location.path());
            $location.url('/players');
         }, function( error ) {
            $scope.hasError = true;
         }
      );
   }
}]);
