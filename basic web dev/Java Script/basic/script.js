let text = document.querySelector("hi").innertext;

console.log(text + "rajvardhan");

function submit(){
    console.log("submit button clicked");

    const nm = document.querySelector("#fullname").value.trim;
    const em = document.querySelector("#email").value;

    console.log("fullname",nm);
    console.log("email",em);
}