var user = null;
var editing = false;

var setUser = function(u){
  $('#profile').text('Welcome ' + u.username + '!');
}

var redirectToLogin = function(){
  window.location.replace('/');
}

var redirectToTasks = function(){
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
      success: function(u){user = u;setUser(u);judgePage(currentPage);},
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
      + tasks[n].id + '"></label></td><td><button class="waves-light waves-effect btn cyan btn-delete"'
      + 'onclick="deleteTask('+ t +')"><i class="material-icons">delete</i></button></td></tr>';
      $('#tasksList').append(t);
      if(tasks[n].completed === 'true' || tasks[n].completed === true){
        $('#' + tasks[n].id).children().eq(3).children().prop('checked', true);
      }
      else{
        $('#' + tasks[n].id).children().eq(3).children().prop('checked', false);
      }
    }
  }
  editing = false;
  var count = 0;
  $('.slideIn').each(function(index, value){
      setTimeout(function(){$('.slideIn').eq(index).addClass('show')}, 0);
   });
}

var showNewTask = function(task){
	var t = "'" + task.id + "'";
    var t = '<tr class="task" id="'
    + task.id + '"><td style="overflow: auto" onclick="editDescription('+ t +')"><div style="float:left">'
    + task.description + '</div>&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-pencil-square-o hvr-grow"></i></td><td><input class="taskAttr" type="color" value="'
    + task.color + '"/></td><td onclick="editDueDate('+ t +')"><div style="float:left">'
    + convertDateFormat2(task.due) + '</div>&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-pencil-square-o hvr-grow"></i></td><td>&nbsp;&nbsp;&nbsp;&nbsp;<input id="c_'
    + task.id + '" type="checkbox" class="taskAttr"/><label for="c_'
    + task.id + '"></label></td><td><button class="waves-light waves-effect btn cyan btn-delete"'
    + 'onclick="deleteTask('+ t +')"><i class="material-icons">delete</i></button></td></tr>';
    $('#tasksList').append(t);
}

var createTask = function(){
  var description = $('#description').val();
  var colorCode = $('#colorCode').val();
  var due = $('#dueDate').val();
  dueDate = convertDateFormat(due);
  if(description && due){
    var body = {description: description, color: colorCode,
      due: dueDate, completed: false};
    url = 'api/users/' + user.id + '/tasks';
    $.ajax(url, {headers: { 
        'Accept': 'application/json',
        'Content-Type': 'application/json' 
    },type: 'POST', data: JSON.stringify(body), success:showNewTask, error: redirectToLogin});
  }
  else if(!description){
    Materialize.toast('Please enter description!', 3000);
  }
  else if(!due){
	 Materialize.toast('Please enter dueDate!', 3000);
  }
}

var deleteAnimation = function(thing){
	if(thing)
		$( "#" + thing.id ).fadeOut("slow", function() {});
	else{
		Materialize.toast('Delete failed', 3000);
	}
}

var deleteTask = function(tid){
  url = 'api/users/' + user.id + '/tasks/' + tid;
  $.ajax(url, {type: 'DELETE', success: deleteAnimation, error: redirectToLogin});
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
    Materialize.toast("Please click '√' to save edition.!", 3000);
  }
}

var editDescriptionDone = function(tid){
  var newD =  $('#' + tid).children().eq(0).children().eq(0).val();
  $('#' + tid).children().eq(0).remove();
  $('#' + tid).children().eq(0).children().eq(0).html(newD);
  $('#' + tid).children().eq(0).attr({"style" : "display:"});
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
	  Materialize.toast("Please click '√' to save edition.!", 3000);
  }
}

var editDueDateDone = function(tid){
  var newD =  $('#' + tid).children().eq(2).children().eq(0).val();
  $('#' + tid).children().eq(2).remove();
  $('#' + tid).children().eq(2).children().eq(0).html(newD);
  $('#' + tid).children().eq(2).attr({"style" : "display:"});
  editing = false;
  updateTaks(tid);
}

var updateTaks = function(tid){
  var description = $("#" + tid).children().eq(0).children().eq(0).html();
  var colorCode = $("#" + tid).children().eq(1).children().val();
  var dueDate = $("#" + tid).children().eq(2).children().eq(0).html();
  dueDate = convertDateFormat(dueDate);
  var complete = $("#" + tid).children().eq(3).children().prop('checked');
  var body = {description: description, color: colorCode,
    due: dueDate, completed: complete, id:tid, ownerId: user.id};
  url = 'api/users/' + user.id + '/tasks/' + tid;
  if(!editing){
    $.ajax(url, {headers: { 
        'Accept': 'application/json',
        'Content-Type': 'application/json' 
    },type: 'PUT', data: JSON.stringify(body), error: redirectToLogin});
  }
  else{
	  Materialize.toast("Please click '√' to save edition.!", 3000);
  }
}

var getAll = function(){
  url = '/api/users/' + user.id + '/tasks';
  url = url + '?incomplete=' + $('#c1').prop('checked') +'&overdue=' + $('#c2').prop('checked');
  $.ajax(url, {type: 'GET', success: showTasks, error: redirectToLogin});
}

var showAllUsers = function(users){
	var content = '';
	for(var i in users){
		u = users[i];
		var uid = "'" + u.id + "'"
		content = 
		'<tr id="'
		+ u.id + '"><td>' 
		+ u.username + '</td><td><input type="checkbox" id="c1_' 
		+ u.id +'" class="userCheck"/><label for="c1_'
		+ u.id + '"></label></td><td><input type="checkbox" id="c2_' 
		+ u.id + '" class="adminCheck"/><label for="c2_' 
		+ u.id + '"></label></td><td><button class="waves-light waves-effect btn cyan btn-delete" onclick="deleteUser('
		+ uid +')"><i class="material-icons">delete</i></button></td></tr>';  
		$("#usersTbody").append(content);
		if(u.username === 'Bilbo'){
			$("#usersTbody").children().eq(0).children().eq(1).children().eq(0).prop('disabled', 'disabled');
			$("#usersTbody").children().eq(0).children().eq(2).children().eq(0).prop('disabled', 'disabled');
			$("#usersTbody").children().eq(0).children().eq(3).html("");
		}
		if((users[i].authorities[0] === undefined || users[i].authorities[0].authority !== 'ROLE_USER') && (users[i].authorities[1] === undefined || users[i].authorities[1].authority !== 'ROLE_USER')){
		  $("#usersTbody").children().eq(i).children().eq(1).children().eq(0).prop('checked', false);
	    }
	    else{
	      $("#usersTbody").children().eq(i).children().eq(1).children().eq(0).prop('checked', true);
	    }
		
		if((users[i].authorities[0] === undefined || users[i].authorities[0].authority !== 'ROLE_ADMIN') && (users[i].authorities[1] === undefined ||users[i].authorities[1].authority !== 'ROLE_ADMIN')){
		  $("#usersTbody").children().eq(i).children().eq(2).children().eq(0).prop('checked', false);
	    }
	    else{
	      $("#usersTbody").children().eq(i).children().eq(2).children().eq(0).prop('checked', true);
	    }
		
	}
	 var count = 0;
	  $('.slideIn').each(function(index, value){
	      setTimeout(function(){$('.slideIn').eq(index).addClass('show')}, 0);
	   });

}

var getAllUsers = function(){
  $.ajax('api/users', 
    {
	  type: 'GET',
	  success: showAllUsers,
	  error: redirectToLogin
    });
}

var showNewUser = function(u){
	if(u){
		var uid = "'" + u.id + "'"
		content = 
		'<tr id="'
		+ u.id + '"><td>' 
		+ u.username + '</td><td><input type="checkbox" id="c1_' 
		+ u.id +'" class="userCheck"/><label for="c1_'
		+ u.id + '"></label></td><td><input type="checkbox" id="c2_' 
		+ u.id + '" class="adminCheck"/><label for="c2_' 
		+ u.id + '"></label></td><td><button class="waves-light waves-effect btn cyan btn-delete" onclick="deleteUser('
		+ uid +')"><i class="material-icons">delete</i></button></td></tr>';  
		$("#usersTbody").append(content);
		
		if((u.authorities[0] = undefined || u.authorities[0].authority !== 'ROLE_USER') && (u.authorities[1] === undefined || u.authorities[1].authority !== 'ROLE_USER')){
		  $("#usersTbody tr:last-child").children().eq(1).children().eq(0).prop('checked', false);
	    }
	    else{
	      $("#usersTbody tr:last-child").children().eq(1).children().eq(0).prop('checked', true);
	    }
		
		if((u.authorities[0] = undefined || u.authorities[0].authority !== 'ROLE_ADMIN') && (u.authorities[1] === undefined || u.authorities[1].authority !== 'ROLE_ADMIN')){
			$("#usersTbody tr:last-child").children().eq(2).children().eq(0).prop('checked', false);
	    }
	    else{
	    	$("#usersTbody tr:last-child").children().eq(2).children().eq(0).prop('checked', true);
	    }
	}
	else{
		Materialize.toast('User already exists!', 3000);
	}
}

var addUser = function(){
  var username = $('#userName').val();
  var body = {username: username}
	 
  $.ajax('api/user', 
	{
	  headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
	  type: "POST", 
	  data: JSON.stringify(body),
	  success: showNewUser,
	  error: redirectToLogin
	})
}

var updateUser = function(t){
	var user = false
	var admin = false;
	if(t.parent().parent().children().eq(1).children().eq(0).prop('checked')){
	  user = true;
	}
	if(t.parent().parent().children().eq(2).children().eq(0).prop('checked')){
	  admin = true;
	}
	console.log(user+" "+admin);
	var username = t.parent().parent().children().eq(0).html();
	var body = {username: username}
	 $.ajax('api/users/' + t.prop('id').substring(3) + '/?user=' + user + '&admin=' + admin, 
	{
	  type: "PUT",
	  success: function(r){
		  if(!r){
			  Materialize.toast('Update failed', 3000);
			  setTimeout(redirectToTasks, 1000);
		  }  
	  }
	})
}

var deleteUser = function(id){
	 $.ajax('api/users/' + id, 
	{
	  type: "DELETE", success: deleteAnimation, error: redirectToLogin 
	})
}

var login = function(){
  var username = $('#username').val();
  var password = $('#password').val();
  body={username: username, password: password};
  url = '/login';
  $.ajax(url, {type: 'POST', data: body, success: redirectToTasks, error: loginError});
}

var logout = function(){
	$.ajax('/logout', {type: 'GET', success: redirectToLogin});
}

var judgePage = function(currentPage){
	if(typeof user == 'string'){
		redirectToLogin();
	}
	
	var isAdmin = false;
	var isUser = false;
	user.roles.forEach(function(e){
		if(e.authority ==="ROLE_ADMIN"){
			isAdmin = true;
		}
		if(e.authority ==="ROLE_USER"){
			isUser = true;
		}
			
	});
	if(currentPage === "taskts"){
		if(isUser && !isAdmin){
			$(".pages").children().eq(1).css('display', 'none');
			getAll();
		}
		else if(!isUser && isAdmin)
			redirectToUserManagement();
		else if(!isUser && !isAdmin)
			redirectToLogin();
		else
			getAll();
	}
	else if(currentPage === "userManagement"){
		if(isUser && !isAdmin)
			redirectToTasks();
		else if(!isUser && isAdmin){
			$(".pages").children().eq(0).css('display', 'none');
			getAllUsers();
		}
		else if(!isUser && !isAdmin)
			redirectToLogin();
		else
		getAllUsers();
	}
}

$(document).on('change','.taskAttr',function(){
    updateTaks($(this).parent().parent().attr('id'));
 });

$(document).on('change','.userCheck',function(){
    updateUser($(this));
 });

$(document).on('change','.adminCheck',function(){
	updateUser($(this));
 });
