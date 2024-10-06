let point=0;
let data=['water water','forest','lake','tree','sunshine'];
const pics=document.querySelectorAll('.pic');
const text=document.createElement('span');
text.classList.add('text');
text.textContent=data[0];
pics[0].classList.add('wide');
pics[0].appendChild(text);

for(let i=0;i<pics.length;i++){
	pics[i].style.backgroundRepeat=`no-repeat`;
	pics[i].style.backgroundImage=`url("pics/${i+1}.jpg")`;
	pics[i].style.backgroundSize='cover';
	pics[i].addEventListener('click',()=>{
		if(i!==point){
			const textElement=document.querySelector('.text');
			pics[point].removeChild(textElement);
			const newTextElement=document.createElement('span');
			newTextElement.textContent=data[i];
			newTextElement.classList.add('text');
			pics[i].classList.add('wide');
			pics[i].appendChild(newTextElement);
			pics[point].classList.remove('wide');
			point=i;
		}
	});
}


