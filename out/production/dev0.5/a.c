#include <stdio.h>

int getint() {
    int ret;
    scanf("%d", &ret);
    return ret;
}
//#include<stdio.h>
const int a = 9;
const int b = a;
int d = a;

//int getint(){
// int n;
// scanf("%d",&n);
// return n;
//
//}
int mycheck(int a){
	if (a == 1) return 10;
	if (a==2) return 20;
	if (a==4) return 40 ;
	else if (a==5) return 50;
	else if (a == 6) return 60;
	if (a==3) return 30 ;else return 99 ;
	return 0;
	return 1;
}

void testGetInt(){
	int i = 0;

	while(i<9){
		int temp;
		temp = getint();
		printf("%d\n",temp);
		i = i + 1;
	}
}


int main(){
	const int in1 = 89;
	const int in2 = in1;
	int vin1 = in1;
	int vin2 = vin1;
	int vin3 = in2;
	int vin4 = a + b;
	int vin5 = d;


	printf("%d\n",19231177);
	testGetInt();
	int a = 1;
	while(a<10){
		printf("%d\n",mycheck(a));
		if (mycheck(a) < mycheck(mycheck(a)/20) ){
			printf("appear\n");
		}else{
			printf("not\n");
		}
		a = a + 1;
	}
	int a1 = 1,a2 = 2,a3 = 3;
	printf("print123\n");
	printf("%d%d%d\n",1,2,3);
	printf("/****/%d%d%d\n",1,a2,a3);
	printf("/**/%d%d%d\n",a1,a2,a3);
	printf("print return value :\n");
	int temp = 5;
	printf("in : 5 ,out = %d\n",mycheck(5));
	printf("in : 5 ,out = %d\n",mycheck(temp));
	return 0;
}
