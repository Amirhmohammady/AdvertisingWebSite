<script>
    function editUser() {
        var xhttp = new XMLHttpRequest();
        xhttp.onload = function () {
			let stts01 = this.status;
            if (stts01 != 200) {
				document.getElementById("statusbar01").getElementsByTagName('span')[0].textContent = this.responseText;
				document.getElementById("statusbar01").style.display="block";
			} else window.location.reload();
        };
		let user = {};
		user["username"] = document.getElementById("epusername").value;
		user["profilename"] = document.getElementById("epprofilename").value;
		user["fullname"] = document.getElementById("epfullname").value;
		user["aboutme"] = document.getElementById("epaboutme").value;
		user["websiteurl"] = document.getElementById("epwebsiteurl").value;
		user["email"] = document.getElementById("epemail").value;
		user["password"] = document.getElementById("eppassword").value;
		user["newpass"] = document.getElementById("epnewpassword").value;
		user = JSON.stringify(user);

		console.log(user);
        xhttp.open("POST", [[@{/restapi/editUser/}]], true);
		xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send(user);
    }
</script>
