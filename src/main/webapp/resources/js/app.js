'use strict';

var myapp = angular
    .module('myApp', ['ngResource', 'ngRoute', 'swaggerUi', 'http-auth-interceptor', 'ngAnimate', 'angular-spinkit', 'ngPassword', 'nvd3', 'chart.js', 'anguFixedHeaderTable']);

myapp.constant('USER_ROLES', {
    all: '*',
    admin: 'admin',
    student: 'student',
    super_user: 'super_user',
    rest: 'rest'
});

myapp.config(['ChartJsProvider', function (ChartJsProvider) {
    // Configure all charts
    ChartJsProvider.setOptions({
        chartColors: ['#FF5252', '#FF8A80', '#80b6ff', '#c980ff'],
        responsive: true,
        scale: {
            pointLabels: {
                fontSize: 15
            }
        }
    });
}]);

myapp.config(function ($routeProvider, USER_ROLES) {

    $routeProvider.when("/home", {
        templateUrl: "partials/home.html",
        controller: 'HomeController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/', {
        redirectTo: '/home'
    }).when('/apiDoc', {
        templateUrl: 'partials/apiDoc.html',
        controller: 'ApiDocController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when('/login', {
        templateUrl: 'partials/login.html',
        controller: 'LoginController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/register', {
        templateUrl: 'partials/register.html',
        controller: 'RegistrationController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when('/agile_tool_admin', {
        templateUrl: 'partials/AgileToolAdmin.html',
        controller: 'AgileToolAdmin',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when('/course/:course_id', {
        templateUrl: 'partials/course.html',
        controller: 'CourseController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/team/:team_id', {
        templateUrl: 'partials/team.html',
        controller: 'TeamController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/student/:student_id', {
        templateUrl: 'partials/student.html',
        controller: 'StudentController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/about', {
        templateUrl: 'partials/about.html',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/metricsGuide', {
        templateUrl: 'partials/metricsGuide.html',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/users', {
        templateUrl: 'partials/users.html',
        controller: 'UsersController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.super_user, USER_ROLES.admin]
        }
    }).when('/studentProfile/:user_id', {
        templateUrl: 'partials/studentProfile.html',
        controller: 'StudentProfileController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/adminProfile/:user_id', {
        templateUrl: 'partials/adminProfile.html',
        controller: 'AdminProfileController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/restProfile/:user_id', {
        templateUrl: 'partials/restProfile.html',
        controller: 'RestProfileController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/loading', {
        templateUrl: 'partials/loading.html',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when("/logout", {
        template: " ",
        controller: "LogoutController",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when("/error/:code", {
        templateUrl: "partials/error.html",
        controller: "ErrorController",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when("/create_students", {
        templateUrl: "partials/create_students.html",
        controller: "newCourseStudents",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when("/create_admins", {
        templateUrl: "partials/create_admins.html",
        controller: "newCourseAdmins",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when("/create_course", {
        templateUrl: "partials/create_course.html",
        controller: "newCourse",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when("/create_teams", {
        templateUrl: "partials/create_teams.html",
        controller: "newCourseTeams",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when("/create_channels", {
        templateUrl: "partials/create_channels.html",
        controller: "newCourseChannels",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when("/provision_course", {
        templateUrl: "partials/provision_course.html",
        controller: "provisionCourse",
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.super_user]
        }
    }).when("/rest", {
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.rest, USER_ROLES.super_user]
        }
    })
        .otherwise({
            redirectTo: '/error/404',
            access: {
                loginRequired: false,
                authorizedRoles: [USER_ROLES.all]
            }
        });
});

myapp.run(function ($rootScope, $location, $http, AuthSharedService, Session, USER_ROLES, $q, $timeout) {

    $rootScope.$on('$routeChangeStart', function (event, next) {

        if (next.originalPath === "/login" && $rootScope.authenticated) {
            event.preventDefault();
        } else if (next.access && next.access.loginRequired && !$rootScope.authenticated) {
            event.preventDefault();
            $rootScope.$broadcast("event:auth-loginRequired", {});
        } else if (next.access && !AuthSharedService.isAuthorized(next.access.authorizedRoles)) {
            event.preventDefault();
            $rootScope.$broadcast("event:auth-forbidden", {});
        }
    });

    $rootScope.$on('$routeChangeSuccess', function (scope, next, current) {
        $rootScope.$evalAsync(function () {
            $.material.init();
        });
    });

    // Call when the the client is confirmed
    $rootScope.$on('event:auth-loginConfirmed', function (event, data) {
        console.log('login confirmed start ' + data);
        $rootScope.loadingAccount = false;
        var nextLocation = ($rootScope.requestedUrl ? $rootScope.requestedUrl : "/home");
        var delay = ($location.path() === "/loading" ? 1500 : 0);

        $timeout(function () {
            Session.create(data);
            $rootScope.account = Session;
            $rootScope.authenticated = true;
            $location.path(nextLocation).replace();
        }, delay);

    });

    // Call when the 401 response is returned by the server
    $rootScope.$on('event:auth-loginRequired', function (event, data) {
        if ($rootScope.loadingAccount && data.status !== 401) {
            $rootScope.requestedUrl = $location.path()
            $location.path('/loading');
        } else {
            Session.invalidate();
            $rootScope.authenticated = false;
            $rootScope.loadingAccount = false;
            $location.path('/login');
        }
    });

    // Call when the 403 response is returned by the server
    $rootScope.$on('event:auth-forbidden', function (rejection) {
        $rootScope.$evalAsync(function () {
            $location.path('/error/403').replace();
        });
    });

    // Call when the user logs out
    $rootScope.$on('event:auth-loginCancelled', function () {
        $location.path('/login').replace();
    });

    // Get already authenticated user account
    AuthSharedService.getAccount();


});





