#include <stdint.h>
#include <stdio.h>

#define BIT(n) (1 << (n))
int hook_id = 0;

void subscribe(uint8_t *n) {
    *n = BIT(hook_id);
}

int main() {

    uint8_t mask;
    subscribe(&mask);
    printf("1: %u\n", mask);

    mask = BIT(mask);
    printf("2: %u\n", mask);

    return 0;
}