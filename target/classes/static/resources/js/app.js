var user = null;
var editing = false;

var setUser = function(u){
  $('#profile').text('Welcome ' + u.username + '!');
}

var redirectToLogin = function(){
  window.location.replace('/');
}

var redirectToTaskts = function(){
  window.location.replace('/tasks.html');
}

var redirectToUserManagement = function(){
	  window.location.replace('/userManagement.html');
	}

var loginError = function(){
  Materialize.toast('Incorrect email or password. Please try again.', 3000);
}

var getPrincipal = function(currentPage){
  $.ajax('/api/user',
    {
      type: 'GET',
      success: function(u){user = u;setUser(u);getAll();judgePage(currentPage);},
      error: redirectToLogin
    });
}

var convertDateFormat = function(date){
  var d = date.split('/');
  return d[2] + '-' + d[0] + '-' + d[1]
}

var convertDateFormat2 = function(date){
  var d = date.split('-');
  return d[1] + '/' + d[2] +'/' + d[0];
}

var showTasks = function(result){
  tasks = result;
  $('#tasksList').html('');
  for(var n in tasks)
  {
    if(tasks[n].description.indexOf($('#search1').val()) != -1)
    {
      var t = "'" + tasks[n].id + "'";
      var t = '<tr class="task"  id="'
      + tasks[n].id + '"><td style="overflow: auto" onclick="editDescription('+ t +')"><div style="float:left">'
      + tasks[n].description + '</div>&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-pencil-square-o hvr-grow"></i></td><td><input class="taskAttr" type="color" value="'
      + tasks[n].color + '"/></td><td onclick="editDueDate('+ t +')"><div style="float:left">'
      + convertDateFormat2(tasks[n].due) + '</div>&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-pencil-square-o hvr-grow"></i></td><td>&nbsp;&nbsp;&nbsp;&nbsp;<input id="c_'
      + tasks[n].id + '" type="checkbox" class="taskAttr"/><label for="c_'
      + tasks[n].id + '"></label></td><td><button class="waves-light waves-effect btn red btn-delete"'
      + 'onclick="deleteTask('+ t +')"><i class="material-icons">delete</i></button></td></tr>';
      $('#tasksList').append(t);
      if(tasks[n].complete === 'true' || tasks[n].complete === true){
        $('#' + tasks[n].id).children().eq(3).children().prop('checked', true);
      }
      else{
        $('#' + tasks[n].id).children().eq(3).children().prop('checked', false);
      }
    }
  }
  editing = false;
}

var createTask = function(){
  var description = $('#description').val();
  var colorCode = $('#colorCode').val();
  var dueDate = $('#dueDate').val();
  dueDate = convertDateFormat(dueDate);
  if(description && dueDate){
    var body = {description: description, color: colorCode,
      due: dueDate, completed: false};
    url = 'api/users/' + user.id + '/tasks';
    $.ajax(url, {headers: { 
        'Accept': 'application/json',
        'Content-Type': 'application/json' 
    },type: 'POST', data: JSON.stringify(body), success: function(e){console.log(e)}});
  }
  else if(!description){
    alert('Please enter description!');
  }
  else if(!dueDate){
    alert('Please enter dueDate!');
  }
}

var deleteAnimation = function(tid){
  $( "#" + tid ).fadeOut("slow", function() {
    getAll();
  });
}

var deleteTask = function(tid){
  url = 'tasker/users/' + user.id + '/tasks/' + tid;
  $.ajax(url, {type: 'DELETE', success: deleteAnimation(tid), error: redirectToLogin});
}

var editDescription = function(tid){
  if(!editing){
    editing = true;
    var t ="'" + tid + "'";
    $('#' + tid).children().eq(0).attr({"style" : "display:none"});
    var i = '<div class="col s4"><input type="text" class="editor" value="'
    + $('#' + tid).children().eq(0).children().eq(0).html()
    + '"><i class="fa fa-check hvr-grow" onclick="editDescriptionDone('+ t +')"></i></div>';
    $('#' + tid).prepend(i);
  }
  else{
    alert("Please click '√' to save edition.");
  }
}

var editDescriptionDone = function(tid){
  var newD =  $('#' + tid).children().eq(0).children().eq(0).val();
  $('#' + tid).children().eq(0).remove();
  $('#' + tid).children().eq(0).children().eq(0).html(newD);
  $('#' + tid).children().eq(0).attr({"style" : "display:inline"});
  editing = false;
  updateTaks(tid);
}

var editDueDate = function(tid){
  if(!editing){
  editing = true;
  var t ="'" + tid + "'";
  $('#' + tid).children().eq(2).attr({"style" : "display:none"});
  var i = '<div class="col s3"><input type="date" class="dateEditor datepicker" value="'
  + $('#' + tid).children().eq(2).children().eq(0).html()
  + '"><i class="fa fa-check hvr-grow" onclick="editDueDateDone('+ t +')"></i></div>';
  $('#' + tid).children().eq(1).after(i);
  $('.datepicker').pickadate({
    selectMonths: true,
    selectYears: 16,
    format: '  mm/dd/yyyy',
  });
  }
  else{
    alert("Please click '√' to save edition.");
  }
}

var editDueDateDone = function(tid){
  var newD =  $('#' + tid).children().eq(2).children().eq(0).val();
  $('#' + tid).children().eq(2).remove();
  $('#' + tid).children().eq(2).children().eq(0).html(newD);
  $('#' + tid).children().eq(2).attr({"style" : "display:inline"});
  editing = false;
  updateTaks(tid);
}

var updateTaks = function(tid){
  var description = $("#" + tid).children().eq(0).children().eq(0).html();
  var colorCode = $("#" + tid).children().eq(1).children().val();
  var dueDate = $("#" + tid).children().eq(2).children().eq(0).html();
  dueDate = convertDateFormat(dueDate);
  var complete = $("#" + tid).children().eq(3).children().prop('checked');
  var body = {description: description, colorCode: colorCode,
    dueDate: dueDate, complete: complete};
  url = 'tasker/users/' + user.id + '/tasks/' + tid;
  if(!editing){
    $.ajax(url, {type: 'PUT', data: body, success: getAll, error: redirectToLogin});
  }
  else{
    alert("Please click '√' to save edition.");
  }
}

var getAll = function(){
  url = '/api/users/' + user.id + '/tasks';
  url = url + '?incomplete=' + $('#c1').prop('checked') +'&overdue=' + $('#c2').prop('checked');
  $.ajax(url, {type: 'GET', success: showTasks, error: redirectToLogin});
}

var adddepth = function(id){
  $( "#" + id ).removeClass('z-depth-1');
  $( "#" + id ).addClass('z-depth-2');
}

var subdepth = function(id){
  $( "#" + id ).removeClass('z-depth-2');
  $( "#" + id ).addClass('z-depth-1');
}

var login = function(){
  var username = $('#username').val();
  var password = $('#password').val();
  body={username: username, password: password};
  url = '/login';
  $.ajax(url, {type: 'POST', data: body, success: redirectToTaskts, error: loginError});
}

var logout = function(){
	$.ajax('/logout', {type: 'GET', success: redirectToLogin});
}

var judgePage = function(currentPage){
	var isAdmin = false;
	user.roles.forEach(function(e){
		if(e.authority ==="ROLE_ADMIN")
			isAdmin = true;
	});
	if(currentPage === "taskts" && isAdmin)
		redirectToUserManagement();
	if(currentPage === "userManagement" && !isAdmin)
		redirectToTasks();
}