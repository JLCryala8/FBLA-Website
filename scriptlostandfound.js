
function screens1(){

  window.location.href = "lostandfound2.html";
}

function screens2() {
  window.location.href = "lostandfound3.html";
}


document.addEventListener("DOMContentLoaded", function() {

  document.getElementById("final1").onclick = function(){
  event.preventDefault();
   
  const names = document.getElementById("text1").value;
  const date = document.getElementById("text2").value;
  const desc = document.getElementById("text3").value;
  const catego = document.getElementById("text4").value;
  const locat = document.getElementById("text5").value;
  
   document.getElementById("output").innerHTML = `
   <p class="outputcode">The Items name: ${names}</p> 
   <p class="outputcode">Item was found on ${date}</p> 
   <p class="outputcode">Item info: ${desc}</p> 
   <p class="outputcode">The type of item is: ${catego}</p> 
   <p class="outputcode">Item was found in: ${locat}</p>`;
     
  }


});