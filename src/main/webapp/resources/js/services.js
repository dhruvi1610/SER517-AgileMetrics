'use strict';

myapp.service('Session', function () {
    this.create = function (data) {
        this.id = data.id;
        this.login = data.login;
        this.firstName = data.firstName;
        this.lastName = data.familyName;
        this.email = data.email;
        this.userRoles = [];
        angular.forEach(data.authorities, function (value, key) {
            this.push(value.name);
        }, this.userRoles);
    };
    this.invalidate = function () {
        this.id = null;
        this.login = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.userRoles = null;
    };
    return this;
});

myapp.service('userService', function () {
    var user = null;
    var auth = null;

    var setUser = function (userObject) {
        user = userObject;
    };

    var getUser = function () {
        return user;
    };

    var setAuth = function (authObject) {
        auth = authObject;
    };

    var getAuth = function () {
        return auth;
    };
    return {
        setUser: setUser,
        getUser: getUser,
        setAuth: setAuth,
        getAuth: getAuth
    };
})
    .service('adminService', function () {
        var course = null;

        var setCourse = function (courseObject) {
            course = courseObject;
        };

        var getCourse = function () {
            return course;
        };
        return {
            setCourse: setCourse,
            getCourse: getCourse
        };
    })
    .service('courseService', function () {
        var course = null;

        var setCourse = function (courseObject) {
            course = courseObject;
        };

        var getCourse = function () {
            return course;
        };
        return {
            setCourse: setCourse,
            getCourse: getCourse
        };
    })
    .service('teamService', function () {
        var team = null;

        var setTeam = function (teamName) {
            team = teamName;
        };

        var getTeam = function () {
            return team;
        };
        return {
            setTeam: setTeam,
            getTeam: getTeam
        };
    })
    .service('studentService', function () {
        var studentEmail = null;
        var studentName = null;

        var setStudentEmail = function (email) {
            studentEmail = email;
        };

        var getStudentEmail = function () {
            return studentEmail;
        };

        var setStudentName = function (studentname) {
            studentName = studentname;
        };

        var getStudentName = function () {
            return studentName;
        };
        return {
            setStudentEmail: setStudentEmail,
            getStudentEmail: getStudentEmail,
            setStudentName: setStudentName,
            getStudentName: getStudentName
        };
    })
    .service('provisionService', function ($rootScope) {
        $rootScope.coursePackage = {
                "admins": null,
                "course": null,
                "end_date": '',
                "slack_token": '',
                "taiga_token": '',
                "teams": null
                //"teams": [{
                    //"taiga_project_slug": '',
                    //"team_name": '',
                    //"channels": null,
                    //"github_repo_id": '',
                    //"slack_team_id": '',
                    //"students": null
                //}]

            };
    })
    .service('courseCreateService', function () {
        var course = null;

        var setCourse = function (courseObject) {
            course = courseObject;
        };

        var getCourse = function () {
            return course;
        };
        return {
            setCourse: setCourse,
            getCourse: getCourse
        };
    })
    .service('teamCreateService', function () {
        var team = null;

        var setTeam = function (teamName) {
            team = teamName;
        };

        var getTeam = function () {
            return team;
        };
        return {
            setTeam: setTeam,
            getTeam: getTeam
        };
    });


myapp.service('AuthSharedService', function ($rootScope, $http, $resource, authService, Session) {
    return {
        login: function (userName, password, rememberMe) {
            var config = {
                ignoreAuthModule: 'ignoreAuthModule',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            };
            $http.post('authenticate', $.param({
                username: userName,
                password: password,
                rememberme: rememberMe
            }), config)
                .success(function (data, status, headers, config) {
                    authService.loginConfirmed(data);
                })
                .error(function (data, status, headers, config) {
                    $rootScope.authenticationError = true;
                    Session.invalidate();
                });
        },
        getAccount: function () {
            $rootScope.loadingAccount = true;
            $http.get('security/account')
                .then(function (response) {
                    authService.loginConfirmed(response.data);
                });
        },
        isAuthorized: function (authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
                if (authorizedRoles == '*') {
                    return true;
                }
                authorizedRoles = [authorizedRoles];
            }
            var isAuthorized = false;
            angular.forEach(authorizedRoles, function (authorizedRole) {
                var authorized = (!!Session.login &&
                Session.userRoles.indexOf(authorizedRole) !== -1);
                if (authorized || authorizedRole == '*') {
                    isAuthorized = true;
                }
            });
            return isAuthorized;
        },
        logout: function () {
            $rootScope.authenticationError = false;
            $rootScope.authenticated = false;
            $rootScope.account = null;
            $http.get('logout');
            Session.invalidate();
            authService.loginCancelled();
        }
    };
});

myapp.service('HomeService', function ($rootScope, $log, $resource) {
    $rootScope.provisionMode = false;
    return {
        getTechno: function () {
            var userResource = $resource('resources/json/techno.json', {}, {
                query: {method: 'GET', params: {}, isArray: true}
            });
            return userResource.query();
        }
    }
});


myapp.service('UsersService', function ($log, $resource) {
    return {
        getAll: function () {
            var userResource = $resource('users', {}, {
                query: {method: 'GET', params: {}, isArray: true}
            });
            return userResource.query();
        }
    }
});


myapp.service('TokensService', function ($log, $resource) {
    return {
        getAll: function () {
            var tokensResource = $resource('security/tokens', {}, {
                query: {method: 'GET', params: {}, isArray: true}
            });
            return tokensResource.query();
        }
    }

});


