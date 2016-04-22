tmod = angular.module('Things.Detail', ['ngRoute', 'ngResource', 'Session']);

tmod.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/things_list/:tid', {
    templateUrl: 'resources/views/players/detail.html',
    controller: 'Things.Detail.Controller'
  });
}]);

tmod.controller('Things.Detail.Controller', ['$scope', '$routeParams', '$resource', 'Session.Service', function($scope, $routeParams, $resource, Session) {
   var Things = $resource('api/users/:uid/things/:tid', {});
   $scope.user = Session.user();
   $scope.thing = Things.get( { uid : Session.user().id, tid : $routeParams['tid'] } );
}]);
