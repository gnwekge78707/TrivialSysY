
#include <stdio.h>

int getint() {
    int ret;
    scanf("%d", &ret);
    return ret;
}

    const int aaa = 1,bbb = 2;
	const int ccc = 3;
	const int ggg = 1+2;
	const int hhh = 1-2;
	const int iii = 1*2;
	const int arr1[2] = {0,1};
	const int arr2[1] = {0};
	const int arr3[2][2] = {
	{1,2},{3,4}}; 
void nothing(){
}
void nothing1(int a){
} 
void nothing2(int a, int b, int c){
}
void nothing3(int a[],int b[][1]){
}
int zero(){
	return 0;
}
int main()
{

	int a;
	int array1[3] = {0,1,2};
	int array2[2][2] = {{1,2},{3,4}}; 
	int b = 1*2;
	int c = 1+1*2;
	int d = b+c;
	int i = 0;
	while (0){
		if (array1[i] == 3){
			break;
		}else{
			continue;
		}
		array1[i] = 1;
		i=i+1;
	}
	array2[1][1] = array1[1];
	if (array2[1][1] == 1){
		array2[1][1] = 1;
	}else {
		array2 [1][1] = 0;
	}
	a=getint();
	b = c+d;
	b = c*d;
	b = c-d;
	b = c+d+d;
	b = c/1;
	d = 1;
	b = c/1;
	b = c%1;
	int m = 1;
	int n = 0;
	int result;
	if(m+n){
		m = 1;
	}
	if(!n){
		m = 1;
	}
	if(m-n){
		m = 1;
	}
	if(m<n){
		m = 1;
	}
	if(m<=n){
		m =1;
	}
	if(m>=n){
		m = 1;
	}
	if(m!=n){
		m =1;
	}

	printf("19231240\n");
	printf("%d\n",a);
	printf("%d\n",array1[0]);
	printf("%d\n",array1[0]);
	printf("%d\n",array1[0]);
	printf("%d\n",array1[0]);
	printf("%d\n",array1[0]);
    return 0;
}