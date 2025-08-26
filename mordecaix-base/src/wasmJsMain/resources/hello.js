function sayHellommmmmmm() {
    console.log("Starting module loading process...");

    // 等待模块加载完成
    AI().then((Module) => {
        console.log("Module loaded successfully:", Module);

        // 模块初始化完成后，调用 Add 函数
        try {
            const result = Module.ccall('Add', // 要调用的函数名
                'number', // 返回值类型
                ['number', 'number'], // 参数类型列表
                [5, 3] // 参数值列表
            );
            console.log("5 + 3 = " + result);
        } catch (error) {
            console.error("Error calling Add function:", error);
        }
    }).catch((error) => {
        console.error("Module failed to load:", error);
    });
    return 123
}