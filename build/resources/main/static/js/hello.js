angular.module('hello', [ 'ngRoute' ]).config(function($routeProvider, $httpProvider) {

    $routeProvider.when('/', {
        templateUrl : 'partials/home.html',
        controller : 'home',
        controllerAs: 'controller'
    }).when('/login', {
        templateUrl : 'partials/login.html',
        controller : 'navigation',
        controllerAs: 'controller'
    }).when('/dashboard', {
        templateUrl : 'partials/dashboard.html',
        controller : 'dashboard',
        controllerAs: 'controller'
    }).when('/about', {
        templateUrl : 'partials/about.html',
        controller : 'about',
        controllerAs: 'controller'
    }).when('/slack', {
    	templateUrl : 'partials/slack-user.html',
    	controller : 'slack',
    	controllerAs : 'controller'
    }).when('/timer', {
    	templateUrl : 'partials/timer.html'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    }).controller('navigation',

    function($rootScope, $http, $location) {

        var self = this

        var authenticate = function(credentials, callback) {

            var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password)
                } : {};

            $http.get('user', {headers : headers}).then(function(response) {
                if (response.data.name) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback();
            }, function() {
                $rootScope.authenticated = false;
                callback && callback();
            });

        }

        authenticate();
        self.credentials = {};
        self.login = function() {
            authenticate(self.credentials, function() {
                if ($rootScope.authenticated) {
                    $location.path("/");
                    self.error = false;
                } else {
                    $location.path("/login");
                    self.error = true;
                }
            })
        };

        self.logout = function() {
            $http.post('logout', {}).finally(function() {
                $rootScope.authenticated = false;
                $location.path("/");
            });
        }
    }).controller('home', function($http) {
        var self = this;
        $http.get('/resource/').then(function(response) {
            self.greeting = response.data;
        })
    }).controller('slack', function($scope, $http) {
    	$http.get('/slack_resource/').then(function(response){
    		$scope.slackData = response.data;
    	})
    });