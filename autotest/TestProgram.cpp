
#include <stdio.h>

int getint() {
    int ret;
    scanf("%d", &ret);
    return ret;
}

void fun_1(){
	return;
}

void fun_2(int a){
	return;
}

void fun_3(int a, int b){
	fun_2(a);
}

void fun_4(int a, int b, int c){
	if(a <= b || a < b || a > b || a >= b){
		if(b <= c && b == a && a != c){

		}
	}

}

void fun_5(int a[], int b[][2]){
	int t1 = 2, t2 = 1, t3=0;
	fun_4(t1, t2, t3 - t2);
	if(t1 + t2 == 0){
		fun_4(t1, t2, t3);
	}else if(t2 + t2 != 0){
		fun_4(t2, t1, t3);
		if(t3 == 1){
			t3 = 0;
		}
	}else{
		return;
	}
}


void fun_6(){
	int var_1;
	var_1 = 1 + 1;
	var_1 = 1 + 1 * 3;
	var_1 = 3 * (var_1 / (-2) + 1 % (23 * +2));
	int a[2][2] = {{1,2}, {1,2}};
	// func_5(a[1], a);
	var_1 = var_1 + var_1 / var_1;
	{
		int var_1;
		int var_2[23];

		var_1 = 0;
		var_1 = 3 % 5;
		var_1 = var_1 * 5;
	}
	return;
}

void fun_7(int a[]){

}

int funInt(int a, int b[][2]){
	int var_1[2][2], var_2[1];
	var_1[1][1] = 2;
	var_2[0] = 3;
	int c = 0, d = 0;
	int i = 0;
	if(c == 0){
		c = 1;
	}
	while(i < a){
		if(i == c + d){
			break;
		} else {
			continue;
		}
		i = i + 1;
	}

	while(i < !a || i >= 0){
		if(i != 0){
			i = i - 2;
		}
		i = i - 1;
	}
	return 1;
}

//	MainFuncDef
int main(){
	int n;
	;
	n = getint();
	fun_6();
	int a[2][2];
	int b[2];
	fun_7(a[1]);
	fun_7(b);
	printf("19373385");
	printf("2");
	printf("3");
	printf("4");
	printf("5");
	printf("6");
	printf("7");
	printf("8");
	printf("9");
	printf("%d", n);
	return 0;
}