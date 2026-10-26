function showalert()
{
    alert("click me button clicked using html events");

}

document.getElementById("clickbutton").addEventListener("click",showalert2);

function showalert2()
{
    alert("click me buttin clicked using js events");
}


const button = document.getElementById("welcomeBtn");
const message = document.getElementById("message");

button.addEventListener("click", function () {
  message.textContent = "Welcome to JavaScript!";
});