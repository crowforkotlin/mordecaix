var AI = (() => {
    var _scriptName = typeof document != 'undefined' ? document.currentScript?.src : undefined;
    if (typeof __filename != 'undefined') _scriptName = _scriptName || __filename;
    return (
        async function (moduleArg = {}) {
            var moduleRtn;

            var Module = moduleArg;
            var readyPromiseResolve, readyPromiseReject;
            var readyPromise = new Promise((resolve, reject) => {
                readyPromiseResolve = resolve;
                readyPromiseReject = reject
            });
            var ENVIRONMENT_IS_WEB = typeof window == "object";
            var ENVIRONMENT_IS_WORKER = typeof WorkerGlobalScope != "undefined";
            var ENVIRONMENT_IS_NODE = typeof process == "object" && typeof process.versions == "object" && typeof process.versions.node == "string" && process.type != "renderer";
            if (ENVIRONMENT_IS_NODE) {
            }
            var arguments_ = [];
            var thisProgram = "./this.program";
            var quit_ = (status, toThrow) => {
                throw toThrow
            };
            var scriptDirectory = "";
            var readAsync, readBinary;
            if (ENVIRONMENT_IS_WEB || ENVIRONMENT_IS_WORKER) {
                if (ENVIRONMENT_IS_WORKER) {
                    scriptDirectory = self.location.href
                } else if (typeof document != "undefined" && document.currentScript) {
                    scriptDirectory = document.currentScript.src
                }
                if (_scriptName) {
                    scriptDirectory = _scriptName
                }
                if (scriptDirectory.startsWith("blob:")) {
                    scriptDirectory = ""
                } else {
                    scriptDirectory = scriptDirectory.slice(0, scriptDirectory.replace(/[?#].*/, "").lastIndexOf("/") + 1)
                }
                {
                    if (ENVIRONMENT_IS_WORKER) {
                        readBinary = url => {
                            var xhr = new XMLHttpRequest;
                            xhr.open("GET", url, false);
                            xhr.responseType = "arraybuffer";
                            xhr.send(null);
                            return new Uint8Array(xhr.response)
                        }
                    }
                    readAsync = async url => {
                        if (isFileURI(url)) {
                            return new Promise((resolve, reject) => {
                                var xhr = new XMLHttpRequest;
                                xhr.open("GET", url, true);
                                xhr.responseType = "arraybuffer";
                                xhr.onload = () => {
                                    if (xhr.status == 200 || xhr.status == 0 && xhr.response) {
                                        resolve(xhr.response);
                                        return
                                    }
                                    reject(xhr.status)
                                };
                                xhr.onerror = reject;
                                xhr.send(null)
                            })
                        }
                        var response = await fetch(url, {credentials: "same-origin"});
                        if (response.ok) {
                            return response.arrayBuffer()
                        }
                        throw new Error(response.status + " : " + response.url)
                    }
                }
            } else {
            }
            var out = console.log.bind(console);
            var err = console.error.bind(console);
            var wasmBinary;
            var wasmMemory;
            var ABORT = false;
            var EXITSTATUS;
            var HEAP8, HEAPU8, HEAP16, HEAPU16, HEAP32, HEAPU32, HEAPF32, HEAP64, HEAPU64, HEAPF64;
            var runtimeInitialized = false;
            var isFileURI = filename => filename.startsWith("file://");

            function updateMemoryViews() {
                var b = wasmMemory.buffer;
                HEAP8 = new Int8Array(b);
                HEAP16 = new Int16Array(b);
                HEAPU8 = new Uint8Array(b);
                HEAPU16 = new Uint16Array(b);
                HEAP32 = new Int32Array(b);
                HEAPU32 = new Uint32Array(b);
                HEAPF32 = new Float32Array(b);
                HEAPF64 = new Float64Array(b);
                HEAP64 = new BigInt64Array(b);
                HEAPU64 = new BigUint64Array(b)
            }

            function preRun() {
                if (Module["preRun"]) {
                    if (typeof Module["preRun"] == "function") Module["preRun"] = [Module["preRun"]];
                    while (Module["preRun"].length) {
                        addOnPreRun(Module["preRun"].shift())
                    }
                }
                callRuntimeCallbacks(onPreRuns)
            }

            function initRuntime() {
                runtimeInitialized = true;
                wasmExports["h"]()
            }

            function postRun() {
                if (Module["postRun"]) {
                    if (typeof Module["postRun"] == "function") Module["postRun"] = [Module["postRun"]];
                    while (Module["postRun"].length) {
                        addOnPostRun(Module["postRun"].shift())
                    }
                }
                callRuntimeCallbacks(onPostRuns)
            }

            var runDependencies = 0;
            var dependenciesFulfilled = null;

            function addRunDependency(id) {
                runDependencies++;
                Module["monitorRunDependencies"]?.(runDependencies)
            }

            function removeRunDependency(id) {
                runDependencies--;
                Module["monitorRunDependencies"]?.(runDependencies);
                if (runDependencies == 0) {
                    if (dependenciesFulfilled) {
                        var callback = dependenciesFulfilled;
                        dependenciesFulfilled = null;
                        callback()
                    }
                }
            }

            function abort(what) {
                Module["onAbort"]?.(what);
                what = "Aborted(" + what + ")";
                err(what);
                ABORT = true;
                what += ". Build with -sASSERTIONS for more info.";
                var e = new WebAssembly.RuntimeError(what);
                readyPromiseReject(e);
                throw e
            }

            var wasmBinaryFile;

            function findWasmBinary() {
                return base64Decode("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")
            }

            function getBinarySync(file) {
                if (ArrayBuffer.isView(file)) {
                    return file
                }
                if (file == wasmBinaryFile && wasmBinary) {
                    return new Uint8Array(wasmBinary)
                }
                if (readBinary) {
                    return readBinary(file)
                }
                throw "both async and sync fetching of the wasm failed"
            }

            async function getWasmBinary(binaryFile) {
                return getBinarySync(binaryFile)
            }

            async function instantiateArrayBuffer(binaryFile, imports) {
                try {
                    var binary = await getWasmBinary(binaryFile);
                    var instance = await WebAssembly.instantiate(binary, imports);
                    return instance
                } catch (reason) {
                    err(`failed to asynchronously prepare wasm: ${reason}`);
                    abort(reason)
                }
            }

            async function instantiateAsync(binary, binaryFile, imports) {
                return instantiateArrayBuffer(binaryFile, imports)
            }

            function getWasmImports() {
                return {a: wasmImports}
            }

            async function createWasm() {
                function receiveInstance(instance, module) {
                    wasmExports = instance.exports;
                    wasmExports = Asyncify.instrumentWasmExports(wasmExports);
                    wasmMemory = wasmExports["g"];
                    updateMemoryViews();
                    removeRunDependency("wasm-instantiate");
                    return wasmExports
                }

                addRunDependency("wasm-instantiate");

                function receiveInstantiationResult(result) {
                    return receiveInstance(result["instance"])
                }

                var info = getWasmImports();
                if (Module["instantiateWasm"]) {
                    return new Promise((resolve, reject) => {
                        Module["instantiateWasm"](info, (mod, inst) => {
                            resolve(receiveInstance(mod, inst))
                        })
                    })
                }
                wasmBinaryFile ??= findWasmBinary();
                try {
                    var result = await instantiateAsync(wasmBinary, wasmBinaryFile, info);
                    var exports = receiveInstantiationResult(result);
                    return exports
                } catch (e) {
                    readyPromiseReject(e);
                    return Promise.reject(e)
                }
            }

            class ExitStatus {
                name = "ExitStatus";

                constructor(status) {
                    this.message = `Program terminated with exit(${status})`;
                    this.status = status
                }
            }

            var callRuntimeCallbacks = callbacks => {
                while (callbacks.length > 0) {
                    callbacks.shift()(Module)
                }
            };
            var onPostRuns = [];
            var addOnPostRun = cb => onPostRuns.push(cb);
            var onPreRuns = [];
            var addOnPreRun = cb => onPreRuns.push(cb);
            var base64Decode = b64 => {
                if (ENVIRONMENT_IS_NODE) {
                    var buf = Buffer.from(b64, "base64");
                    return new Uint8Array(buf.buffer, buf.byteOffset, buf.length)
                }
                var b1, b2, i = 0, j = 0, bLength = b64.length;
                var output = new Uint8Array((bLength * 3 >> 2) - (b64[bLength - 2] == "=") - (b64[bLength - 1] == "="));
                for (; i < bLength; i += 4, j += 3) {
                    b1 = base64ReverseLookup[b64.charCodeAt(i + 1)];
                    b2 = base64ReverseLookup[b64.charCodeAt(i + 2)];
                    output[j] = base64ReverseLookup[b64.charCodeAt(i)] << 2 | b1 >> 4;
                    output[j + 1] = b1 << 4 | b2 >> 2;
                    output[j + 2] = b2 << 6 | base64ReverseLookup[b64.charCodeAt(i + 3)]
                }
                return output
            };

            function getValue(ptr, type = "i8") {
                if (type.endsWith("*")) type = "*";
                switch (type) {
                    case"i1":
                        return HEAP8[ptr];
                    case"i8":
                        return HEAP8[ptr];
                    case"i16":
                        return HEAP16[ptr >> 1];
                    case"i32":
                        return HEAP32[ptr >> 2];
                    case"i64":
                        return HEAP64[ptr >> 3];
                    case"float":
                        return HEAPF32[ptr >> 2];
                    case"double":
                        return HEAPF64[ptr >> 3];
                    case"*":
                        return HEAPU32[ptr >> 2];
                    default:
                        abort(`invalid type for getValue: ${type}`)
                }
            }

            var noExitRuntime = true;
            var stackRestore = val => __emscripten_stack_restore(val);
            var stackSave = () => _emscripten_stack_get_current();

            function _emscripten_fetch_free(id) {
                if (Fetch.xhrs.has(id)) {
                    var xhr = Fetch.xhrs.get(id);
                    Fetch.xhrs.free(id);
                    if (xhr.readyState > 0 && xhr.readyState < 4) {
                        xhr.abort()
                    }
                }
            }

            var _emscripten_is_main_browser_thread = () => !ENVIRONMENT_IS_WORKER;
            var abortOnCannotGrowMemory = requestedSize => {
                abort("OOM")
            };
            var _emscripten_resize_heap = requestedSize => {
                var oldSize = HEAPU8.length;
                requestedSize >>>= 0;
                abortOnCannotGrowMemory(requestedSize)
            };
            var handleException = e => {
                if (e instanceof ExitStatus || e == "unwind") {
                    return EXITSTATUS
                }
                quit_(1, e)
            };
            var runtimeKeepaliveCounter = 0;
            var keepRuntimeAlive = () => noExitRuntime || runtimeKeepaliveCounter > 0;
            var _proc_exit = code => {
                EXITSTATUS = code;
                if (!keepRuntimeAlive()) {
                    Module["onExit"]?.(code);
                    ABORT = true
                }
                quit_(code, new ExitStatus(code))
            };
            var exitJS = (status, implicit) => {
                EXITSTATUS = status;
                _proc_exit(status)
            };
            var _exit = exitJS;
            var maybeExit = () => {
                if (!keepRuntimeAlive()) {
                    try {
                        _exit(EXITSTATUS)
                    } catch (e) {
                        handleException(e)
                    }
                }
            };
            var callUserCallback = func => {
                if (ABORT) {
                    return
                }
                try {
                    func();
                    maybeExit()
                } catch (e) {
                    handleException(e)
                }
            };
            var safeSetTimeout = (func, timeout) => setTimeout(() => {
                callUserCallback(func)
            }, timeout);
            var _emscripten_sleep = ms => Asyncify.handleSleep(wakeUp => safeSetTimeout(wakeUp, ms));
            _emscripten_sleep.isAsync = true;

            class HandleAllocator {
                allocated = [undefined];
                freelist = [];

                get(id) {
                    return this.allocated[id]
                }

                has(id) {
                    return this.allocated[id] !== undefined
                }

                allocate(handle) {
                    var id = this.freelist.pop() || this.allocated.length;
                    this.allocated[id] = handle;
                    return id
                }

                free(id) {
                    this.allocated[id] = undefined;
                    this.freelist.push(id)
                }
            }

            var Fetch = {
                openDatabase(dbname, dbversion, onsuccess, onerror) {
                    try {
                        var openRequest = indexedDB.open(dbname, dbversion)
                    } catch (e) {
                        return onerror(e)
                    }
                    openRequest.onupgradeneeded = event => {
                        var db = event.target.result;
                        if (db.objectStoreNames.contains("FILES")) {
                            db.deleteObjectStore("FILES")
                        }
                        db.createObjectStore("FILES")
                    };
                    openRequest.onsuccess = event => onsuccess(event.target.result);
                    openRequest.onerror = onerror
                }, init() {
                    Fetch.xhrs = new HandleAllocator;
                    var onsuccess = db => {
                        Fetch.dbInstance = db;
                        removeRunDependency("library_fetch_init")
                    };
                    var onerror = () => {
                        Fetch.dbInstance = false;
                        removeRunDependency("library_fetch_init")
                    };
                    addRunDependency("library_fetch_init");
                    Fetch.openDatabase("emscripten_filesystem", 1, onsuccess, onerror)
                }
            };
            var UTF8Decoder = typeof TextDecoder != "undefined" ? new TextDecoder : undefined;
            var UTF8ArrayToString = (heapOrArray, idx = 0, maxBytesToRead = NaN) => {
                var endIdx = idx + maxBytesToRead;
                var endPtr = idx;
                while (heapOrArray[endPtr] && !(endPtr >= endIdx)) ++endPtr;
                if (endPtr - idx > 16 && heapOrArray.buffer && UTF8Decoder) {
                    return UTF8Decoder.decode(heapOrArray.subarray(idx, endPtr))
                }
                var str = "";
                while (idx < endPtr) {
                    var u0 = heapOrArray[idx++];
                    if (!(u0 & 128)) {
                        str += String.fromCharCode(u0);
                        continue
                    }
                    var u1 = heapOrArray[idx++] & 63;
                    if ((u0 & 224) == 192) {
                        str += String.fromCharCode((u0 & 31) << 6 | u1);
                        continue
                    }
                    var u2 = heapOrArray[idx++] & 63;
                    if ((u0 & 240) == 224) {
                        u0 = (u0 & 15) << 12 | u1 << 6 | u2
                    } else {
                        u0 = (u0 & 7) << 18 | u1 << 12 | u2 << 6 | heapOrArray[idx++] & 63
                    }
                    if (u0 < 65536) {
                        str += String.fromCharCode(u0)
                    } else {
                        var ch = u0 - 65536;
                        str += String.fromCharCode(55296 | ch >> 10, 56320 | ch & 1023)
                    }
                }
                return str
            };
            var UTF8ToString = (ptr, maxBytesToRead) => ptr ? UTF8ArrayToString(HEAPU8, ptr, maxBytesToRead) : "";

            function fetchXHR(fetch, onsuccess, onerror, onprogress, onreadystatechange) {
                var url = HEAPU32[fetch + 8 >> 2];
                if (!url) {
                    onerror(fetch, 0, "no url specified!");
                    return
                }
                var url_ = UTF8ToString(url);
                var fetch_attr = fetch + 108;
                var requestMethod = UTF8ToString(fetch_attr + 0);
                requestMethod ||= "GET";
                var timeoutMsecs = HEAPU32[fetch_attr + 56 >> 2];
                var userName = HEAPU32[fetch_attr + 68 >> 2];
                var password = HEAPU32[fetch_attr + 72 >> 2];
                var requestHeaders = HEAPU32[fetch_attr + 76 >> 2];
                var overriddenMimeType = HEAPU32[fetch_attr + 80 >> 2];
                var dataPtr = HEAPU32[fetch_attr + 84 >> 2];
                var dataLength = HEAPU32[fetch_attr + 88 >> 2];
                var fetchAttributes = HEAPU32[fetch_attr + 52 >> 2];
                var fetchAttrLoadToMemory = !!(fetchAttributes & 1);
                var fetchAttrStreamData = !!(fetchAttributes & 2);
                var fetchAttrSynchronous = !!(fetchAttributes & 64);
                var userNameStr = userName ? UTF8ToString(userName) : undefined;
                var passwordStr = password ? UTF8ToString(password) : undefined;
                var xhr = new XMLHttpRequest;
                xhr.withCredentials = !!HEAPU8[fetch_attr + 60];
                xhr.open(requestMethod, url_, !fetchAttrSynchronous, userNameStr, passwordStr);
                if (!fetchAttrSynchronous) xhr.timeout = timeoutMsecs;
                xhr.url_ = url_;
                xhr.responseType = "arraybuffer";
                if (overriddenMimeType) {
                    var overriddenMimeTypeStr = UTF8ToString(overriddenMimeType);
                    xhr.overrideMimeType(overriddenMimeTypeStr)
                }
                if (requestHeaders) {
                    for (; ;) {
                        var key = HEAPU32[requestHeaders >> 2];
                        if (!key) break;
                        var value = HEAPU32[requestHeaders + 4 >> 2];
                        if (!value) break;
                        requestHeaders += 8;
                        var keyStr = UTF8ToString(key);
                        var valueStr = UTF8ToString(value);
                        xhr.setRequestHeader(keyStr, valueStr)
                    }
                }
                var id = Fetch.xhrs.allocate(xhr);
                HEAPU32[fetch >> 2] = id;
                var data = dataPtr && dataLength ? HEAPU8.slice(dataPtr, dataPtr + dataLength) : null;

                function saveResponseAndStatus() {
                    var ptr = 0;
                    var ptrLen = 0;
                    if (xhr.response && fetchAttrLoadToMemory && HEAPU32[fetch + 12 >> 2] === 0) {
                        ptrLen = xhr.response.byteLength
                    }
                    if (ptrLen > 0) {
                        ptr = _malloc(ptrLen);
                        HEAPU8.set(new Uint8Array(xhr.response), ptr)
                    }
                    HEAPU32[fetch + 12 >> 2] = ptr;
                    writeI53ToI64(fetch + 16, ptrLen);
                    writeI53ToI64(fetch + 24, 0);
                    var len = xhr.response ? xhr.response.byteLength : 0;
                    if (len) {
                        writeI53ToI64(fetch + 32, len)
                    }
                    HEAP16[fetch + 40 >> 1] = xhr.readyState;
                    HEAP16[fetch + 42 >> 1] = xhr.status;
                    if (xhr.statusText) stringToUTF8(xhr.statusText, fetch + 44, 64)
                }

                xhr.onload = e => {
                    if (!Fetch.xhrs.has(id)) {
                        return
                    }
                    saveResponseAndStatus();
                    if (xhr.status >= 200 && xhr.status < 300) {
                        onsuccess?.(fetch, xhr, e)
                    } else {
                        onerror?.(fetch, xhr, e)
                    }
                };
                xhr.onerror = e => {
                    if (!Fetch.xhrs.has(id)) {
                        return
                    }
                    saveResponseAndStatus();
                    onerror?.(fetch, xhr, e)
                };
                xhr.ontimeout = e => {
                    if (!Fetch.xhrs.has(id)) {
                        return
                    }
                    onerror?.(fetch, xhr, e)
                };
                xhr.onprogress = e => {
                    if (!Fetch.xhrs.has(id)) {
                        return
                    }
                    var ptrLen = fetchAttrLoadToMemory && fetchAttrStreamData && xhr.response ? xhr.response.byteLength : 0;
                    var ptr = 0;
                    if (ptrLen > 0 && fetchAttrLoadToMemory && fetchAttrStreamData) {
                        ptr = _malloc(ptrLen);
                        HEAPU8.set(new Uint8Array(xhr.response), ptr)
                    }
                    HEAPU32[fetch + 12 >> 2] = ptr;
                    writeI53ToI64(fetch + 16, ptrLen);
                    writeI53ToI64(fetch + 24, e.loaded - ptrLen);
                    writeI53ToI64(fetch + 32, e.total);
                    HEAP16[fetch + 40 >> 1] = xhr.readyState;
                    if (xhr.readyState >= 3 && xhr.status === 0 && e.loaded > 0) xhr.status = 200;
                    HEAP16[fetch + 42 >> 1] = xhr.status;
                    if (xhr.statusText) stringToUTF8(xhr.statusText, fetch + 44, 64);
                    onprogress?.(fetch, xhr, e);
                    if (ptr) {
                        _free(ptr)
                    }
                };
                xhr.onreadystatechange = e => {
                    if (!Fetch.xhrs.has(id)) {
                        return
                    }
                    HEAP16[fetch + 40 >> 1] = xhr.readyState;
                    if (xhr.readyState >= 2) {
                        HEAP16[fetch + 42 >> 1] = xhr.status
                    }
                    onreadystatechange?.(fetch, xhr, e)
                };
                try {
                    xhr.send(data)
                } catch (e) {
                    onerror?.(fetch, xhr, e)
                }
            }

            var writeI53ToI64 = (ptr, num) => {
                HEAPU32[ptr >> 2] = num;
                var lower = HEAPU32[ptr >> 2];
                HEAPU32[ptr + 4 >> 2] = (num - lower) / 4294967296
            };
            var stringToUTF8Array = (str, heap, outIdx, maxBytesToWrite) => {
                if (!(maxBytesToWrite > 0)) return 0;
                var startIdx = outIdx;
                var endIdx = outIdx + maxBytesToWrite - 1;
                for (var i = 0; i < str.length; ++i) {
                    var u = str.charCodeAt(i);
                    if (u >= 55296 && u <= 57343) {
                        var u1 = str.charCodeAt(++i);
                        u = 65536 + ((u & 1023) << 10) | u1 & 1023
                    }
                    if (u <= 127) {
                        if (outIdx >= endIdx) break;
                        heap[outIdx++] = u
                    } else if (u <= 2047) {
                        if (outIdx + 1 >= endIdx) break;
                        heap[outIdx++] = 192 | u >> 6;
                        heap[outIdx++] = 128 | u & 63
                    } else if (u <= 65535) {
                        if (outIdx + 2 >= endIdx) break;
                        heap[outIdx++] = 224 | u >> 12;
                        heap[outIdx++] = 128 | u >> 6 & 63;
                        heap[outIdx++] = 128 | u & 63
                    } else {
                        if (outIdx + 3 >= endIdx) break;
                        heap[outIdx++] = 240 | u >> 18;
                        heap[outIdx++] = 128 | u >> 12 & 63;
                        heap[outIdx++] = 128 | u >> 6 & 63;
                        heap[outIdx++] = 128 | u & 63
                    }
                }
                heap[outIdx] = 0;
                return outIdx - startIdx
            };
            var stringToUTF8 = (str, outPtr, maxBytesToWrite) => stringToUTF8Array(str, HEAPU8, outPtr, maxBytesToWrite);

            function fetchCacheData(db, fetch, data, onsuccess, onerror) {
                if (!db) {
                    onerror(fetch, 0, "IndexedDB not available!");
                    return
                }
                var fetch_attr = fetch + 108;
                var destinationPath = HEAPU32[fetch_attr + 64 >> 2];
                destinationPath ||= HEAPU32[fetch + 8 >> 2];
                var destinationPathStr = UTF8ToString(destinationPath);
                try {
                    var transaction = db.transaction(["FILES"], "readwrite");
                    var packages = transaction.objectStore("FILES");
                    var putRequest = packages.put(data, destinationPathStr);
                    putRequest.onsuccess = event => {
                        HEAP16[fetch + 40 >> 1] = 4;
                        HEAP16[fetch + 42 >> 1] = 200;
                        stringToUTF8("OK", fetch + 44, 64);
                        onsuccess(fetch, 0, destinationPathStr)
                    };
                    putRequest.onerror = error => {
                        HEAP16[fetch + 40 >> 1] = 4;
                        HEAP16[fetch + 42 >> 1] = 413;
                        stringToUTF8("Payload Too Large", fetch + 44, 64);
                        onerror(fetch, 0, error)
                    }
                } catch (e) {
                    onerror(fetch, 0, e)
                }
            }

            function fetchLoadCachedData(db, fetch, onsuccess, onerror) {
                if (!db) {
                    onerror(fetch, 0, "IndexedDB not available!");
                    return
                }
                var fetch_attr = fetch + 108;
                var path = HEAPU32[fetch_attr + 64 >> 2];
                path ||= HEAPU32[fetch + 8 >> 2];
                var pathStr = UTF8ToString(path);
                try {
                    var transaction = db.transaction(["FILES"], "readonly");
                    var packages = transaction.objectStore("FILES");
                    var getRequest = packages.get(pathStr);
                    getRequest.onsuccess = event => {
                        if (event.target.result) {
                            var value = event.target.result;
                            var len = value.byteLength || value.length;
                            var ptr = _malloc(len);
                            HEAPU8.set(new Uint8Array(value), ptr);
                            HEAPU32[fetch + 12 >> 2] = ptr;
                            writeI53ToI64(fetch + 16, len);
                            writeI53ToI64(fetch + 24, 0);
                            writeI53ToI64(fetch + 32, len);
                            HEAP16[fetch + 40 >> 1] = 4;
                            HEAP16[fetch + 42 >> 1] = 200;
                            stringToUTF8("OK", fetch + 44, 64);
                            onsuccess(fetch, 0, value)
                        } else {
                            HEAP16[fetch + 40 >> 1] = 4;
                            HEAP16[fetch + 42 >> 1] = 404;
                            stringToUTF8("Not Found", fetch + 44, 64);
                            onerror(fetch, 0, "no data")
                        }
                    };
                    getRequest.onerror = error => {
                        HEAP16[fetch + 40 >> 1] = 4;
                        HEAP16[fetch + 42 >> 1] = 404;
                        stringToUTF8("Not Found", fetch + 44, 64);
                        onerror(fetch, 0, error)
                    }
                } catch (e) {
                    onerror(fetch, 0, e)
                }
            }

            function fetchDeleteCachedData(db, fetch, onsuccess, onerror) {
                if (!db) {
                    onerror(fetch, 0, "IndexedDB not available!");
                    return
                }
                var fetch_attr = fetch + 108;
                var path = HEAPU32[fetch_attr + 64 >> 2];
                path ||= HEAPU32[fetch + 8 >> 2];
                var pathStr = UTF8ToString(path);
                try {
                    var transaction = db.transaction(["FILES"], "readwrite");
                    var packages = transaction.objectStore("FILES");
                    var request = packages.delete(pathStr);
                    request.onsuccess = event => {
                        var value = event.target.result;
                        HEAPU32[fetch + 12 >> 2] = 0;
                        writeI53ToI64(fetch + 16, 0);
                        writeI53ToI64(fetch + 24, 0);
                        writeI53ToI64(fetch + 32, 0);
                        HEAP16[fetch + 40 >> 1] = 4;
                        HEAP16[fetch + 42 >> 1] = 200;
                        stringToUTF8("OK", fetch + 44, 64);
                        onsuccess(fetch, 0, value)
                    };
                    request.onerror = error => {
                        HEAP16[fetch + 40 >> 1] = 4;
                        HEAP16[fetch + 42 >> 1] = 404;
                        stringToUTF8("Not Found", fetch + 44, 64);
                        onerror(fetch, 0, error)
                    }
                } catch (e) {
                    onerror(fetch, 0, e)
                }
            }

            function _emscripten_start_fetch(fetch, successcb, errorcb, progresscb, readystatechangecb) {
                var fetch_attr = fetch + 108;
                var onsuccess = HEAPU32[fetch_attr + 36 >> 2];
                var onerror = HEAPU32[fetch_attr + 40 >> 2];
                var onprogress = HEAPU32[fetch_attr + 44 >> 2];
                var onreadystatechange = HEAPU32[fetch_attr + 48 >> 2];
                var fetchAttributes = HEAPU32[fetch_attr + 52 >> 2];
                var fetchAttrSynchronous = !!(fetchAttributes & 64);

                function doCallback(f) {
                    if (fetchAttrSynchronous) {
                        f()
                    } else {
                        callUserCallback(f)
                    }
                }

                var reportSuccess = (fetch, xhr, e) => {
                    doCallback(() => {
                        if (onsuccess) (a1 => dynCall_vi(onsuccess, a1))(fetch); else successcb?.(fetch)
                    })
                };
                var reportProgress = (fetch, xhr, e) => {
                    doCallback(() => {
                        if (onprogress) (a1 => dynCall_vi(onprogress, a1))(fetch); else progresscb?.(fetch)
                    })
                };
                var reportError = (fetch, xhr, e) => {
                    doCallback(() => {
                        if (onerror) (a1 => dynCall_vi(onerror, a1))(fetch); else errorcb?.(fetch)
                    })
                };
                var reportReadyStateChange = (fetch, xhr, e) => {
                    doCallback(() => {
                        if (onreadystatechange) (a1 => dynCall_vi(onreadystatechange, a1))(fetch); else readystatechangecb?.(fetch)
                    })
                };
                var performUncachedXhr = (fetch, xhr, e) => {
                    fetchXHR(fetch, reportSuccess, reportError, reportProgress, reportReadyStateChange)
                };
                var cacheResultAndReportSuccess = (fetch, xhr, e) => {
                    var storeSuccess = (fetch, xhr, e) => {
                        doCallback(() => {
                            if (onsuccess) (a1 => dynCall_vi(onsuccess, a1))(fetch); else successcb?.(fetch)
                        })
                    };
                    var storeError = (fetch, xhr, e) => {
                        doCallback(() => {
                            if (onsuccess) (a1 => dynCall_vi(onsuccess, a1))(fetch); else successcb?.(fetch)
                        })
                    };
                    fetchCacheData(Fetch.dbInstance, fetch, xhr.response, storeSuccess, storeError)
                };
                var performCachedXhr = (fetch, xhr, e) => {
                    fetchXHR(fetch, cacheResultAndReportSuccess, reportError, reportProgress, reportReadyStateChange)
                };
                var requestMethod = UTF8ToString(fetch_attr + 0);
                var fetchAttrReplace = !!(fetchAttributes & 16);
                var fetchAttrPersistFile = !!(fetchAttributes & 4);
                var fetchAttrNoDownload = !!(fetchAttributes & 32);
                if (requestMethod === "EM_IDB_STORE") {
                    var ptr = HEAPU32[fetch_attr + 84 >> 2];
                    var size = HEAPU32[fetch_attr + 88 >> 2];
                    fetchCacheData(Fetch.dbInstance, fetch, HEAPU8.slice(ptr, ptr + size), reportSuccess, reportError)
                } else if (requestMethod === "EM_IDB_DELETE") {
                    fetchDeleteCachedData(Fetch.dbInstance, fetch, reportSuccess, reportError)
                } else if (!fetchAttrReplace) {
                    fetchLoadCachedData(Fetch.dbInstance, fetch, reportSuccess, fetchAttrNoDownload ? reportError : fetchAttrPersistFile ? performCachedXhr : performUncachedXhr)
                } else if (!fetchAttrNoDownload) {
                    fetchXHR(fetch, fetchAttrPersistFile ? cacheResultAndReportSuccess : reportSuccess, reportError, reportProgress, reportReadyStateChange)
                } else {
                    return 0
                }
                return fetch
            }

            var printCharBuffers = [null, [], []];
            var printChar = (stream, curr) => {
                var buffer = printCharBuffers[stream];
                if (curr === 0 || curr === 10) {
                    (stream === 1 ? out : err)(UTF8ArrayToString(buffer));
                    buffer.length = 0
                } else {
                    buffer.push(curr)
                }
            };
            var _fd_write = (fd, iov, iovcnt, pnum) => {
                var num = 0;
                for (var i = 0; i < iovcnt; i++) {
                    var ptr = HEAPU32[iov >> 2];
                    var len = HEAPU32[iov + 4 >> 2];
                    iov += 8;
                    for (var j = 0; j < len; j++) {
                        printChar(fd, HEAPU8[ptr + j])
                    }
                    num += len
                }
                HEAPU32[pnum >> 2] = num;
                return 0
            };
            var runAndAbortIfError = func => {
                try {
                    return func()
                } catch (e) {
                    abort(e)
                }
            };
            var runtimeKeepalivePush = () => {
                runtimeKeepaliveCounter += 1
            };
            var runtimeKeepalivePop = () => {
                runtimeKeepaliveCounter -= 1
            };
            var Asyncify = {
                instrumentWasmImports(imports) {
                    var importPattern = /^(invoke_.*|__asyncjs__.*)$/;
                    for (let [x, original] of Object.entries(imports)) {
                        if (typeof original == "function") {
                            let isAsyncifyImport = original.isAsync || importPattern.test(x)
                        }
                    }
                },
                instrumentWasmExports(exports) {
                    var ret = {};
                    for (let [x, original] of Object.entries(exports)) {
                        if (typeof original == "function") {
                            ret[x] = (...args) => {
                                Asyncify.exportCallStack.push(x);
                                try {
                                    return original(...args)
                                } finally {
                                    if (!ABORT) {
                                        var y = Asyncify.exportCallStack.pop();
                                        Asyncify.maybeStopUnwind()
                                    }
                                }
                            }
                        } else {
                            ret[x] = original
                        }
                    }
                    return ret
                },
                State: {Normal: 0, Unwinding: 1, Rewinding: 2, Disabled: 3},
                state: 0,
                StackSize: 4096,
                currData: null,
                handleSleepReturnValue: 0,
                exportCallStack: [],
                callStackNameToId: {},
                callStackIdToName: {},
                callStackId: 0,
                asyncPromiseHandlers: null,
                sleepCallbacks: [],
                getCallStackId(funcName) {
                    var id = Asyncify.callStackNameToId[funcName];
                    if (id === undefined) {
                        id = Asyncify.callStackId++;
                        Asyncify.callStackNameToId[funcName] = id;
                        Asyncify.callStackIdToName[id] = funcName
                    }
                    return id
                },
                maybeStopUnwind() {
                    if (Asyncify.currData && Asyncify.state === Asyncify.State.Unwinding && Asyncify.exportCallStack.length === 0) {
                        Asyncify.state = Asyncify.State.Normal;
                        runAndAbortIfError(_asyncify_stop_unwind);
                        if (typeof Fibers != "undefined") {
                            Fibers.trampoline()
                        }
                    }
                },
                whenDone() {
                    return new Promise((resolve, reject) => {
                        Asyncify.asyncPromiseHandlers = {resolve, reject}
                    })
                },
                allocateData() {
                    var ptr = _malloc(12 + Asyncify.StackSize);
                    Asyncify.setDataHeader(ptr, ptr + 12, Asyncify.StackSize);
                    Asyncify.setDataRewindFunc(ptr);
                    return ptr
                },
                setDataHeader(ptr, stack, stackSize) {
                    HEAPU32[ptr >> 2] = stack;
                    HEAPU32[ptr + 4 >> 2] = stack + stackSize
                },
                setDataRewindFunc(ptr) {
                    var bottomOfCallStack = Asyncify.exportCallStack[0];
                    var rewindId = Asyncify.getCallStackId(bottomOfCallStack);
                    HEAP32[ptr + 8 >> 2] = rewindId
                },
                getDataRewindFuncName(ptr) {
                    var id = HEAP32[ptr + 8 >> 2];
                    var name = Asyncify.callStackIdToName[id];
                    return name
                },
                getDataRewindFunc(name) {
                    var func = wasmExports[name];
                    return func
                },
                doRewind(ptr) {
                    var name = Asyncify.getDataRewindFuncName(ptr);
                    var func = Asyncify.getDataRewindFunc(name);
                    return func()
                },
                handleSleep(startAsync) {
                    if (ABORT) return;
                    if (Asyncify.state === Asyncify.State.Normal) {
                        var reachedCallback = false;
                        var reachedAfterCallback = false;
                        startAsync((handleSleepReturnValue = 0) => {
                            if (ABORT) return;
                            Asyncify.handleSleepReturnValue = handleSleepReturnValue;
                            reachedCallback = true;
                            if (!reachedAfterCallback) {
                                return
                            }
                            Asyncify.state = Asyncify.State.Rewinding;
                            runAndAbortIfError(() => _asyncify_start_rewind(Asyncify.currData));
                            if (typeof MainLoop != "undefined" && MainLoop.func) {
                                MainLoop.resume()
                            }
                            var asyncWasmReturnValue, isError = false;
                            try {
                                asyncWasmReturnValue = Asyncify.doRewind(Asyncify.currData)
                            } catch (err) {
                                asyncWasmReturnValue = err;
                                isError = true
                            }
                            var handled = false;
                            if (!Asyncify.currData) {
                                var asyncPromiseHandlers = Asyncify.asyncPromiseHandlers;
                                if (asyncPromiseHandlers) {
                                    Asyncify.asyncPromiseHandlers = null;
                                    (isError ? asyncPromiseHandlers.reject : asyncPromiseHandlers.resolve)(asyncWasmReturnValue);
                                    handled = true
                                }
                            }
                            if (isError && !handled) {
                                throw asyncWasmReturnValue
                            }
                        });
                        reachedAfterCallback = true;
                        if (!reachedCallback) {
                            Asyncify.state = Asyncify.State.Unwinding;
                            Asyncify.currData = Asyncify.allocateData();
                            if (typeof MainLoop != "undefined" && MainLoop.func) {
                                MainLoop.pause()
                            }
                            runAndAbortIfError(() => _asyncify_start_unwind(Asyncify.currData))
                        }
                    } else if (Asyncify.state === Asyncify.State.Rewinding) {
                        Asyncify.state = Asyncify.State.Normal;
                        runAndAbortIfError(_asyncify_stop_rewind);
                        _free(Asyncify.currData);
                        Asyncify.currData = null;
                        Asyncify.sleepCallbacks.forEach(callUserCallback)
                    } else {
                        abort(`invalid state: ${Asyncify.state}`)
                    }
                    return Asyncify.handleSleepReturnValue
                },
                handleAsync(startAsync) {
                    return Asyncify.handleSleep(wakeUp => {
                        startAsync().then(wakeUp)
                    })
                }
            };
            var getCFunc = ident => {
                var func = Module["_" + ident];
                return func
            };
            var writeArrayToMemory = (array, buffer) => {
                HEAP8.set(array, buffer)
            };
            var lengthBytesUTF8 = str => {
                var len = 0;
                for (var i = 0; i < str.length; ++i) {
                    var c = str.charCodeAt(i);
                    if (c <= 127) {
                        len++
                    } else if (c <= 2047) {
                        len += 2
                    } else if (c >= 55296 && c <= 57343) {
                        len += 4;
                        ++i
                    } else {
                        len += 3
                    }
                }
                return len
            };
            var stackAlloc = sz => __emscripten_stack_alloc(sz);
            var stringToUTF8OnStack = str => {
                var size = lengthBytesUTF8(str) + 1;
                var ret = stackAlloc(size);
                stringToUTF8(str, ret, size);
                return ret
            };
            var ccall = (ident, returnType, argTypes, args, opts) => {
                var toC = {
                    string: str => {
                        var ret = 0;
                        if (str !== null && str !== undefined && str !== 0) {
                            ret = stringToUTF8OnStack(str)
                        }
                        return ret
                    }, array: arr => {
                        var ret = stackAlloc(arr.length);
                        writeArrayToMemory(arr, ret);
                        return ret
                    }
                };

                function convertReturnValue(ret) {
                    if (returnType === "string") {
                        return UTF8ToString(ret)
                    }
                    if (returnType === "boolean") return Boolean(ret);
                    return ret
                }

                var func = getCFunc(ident);
                var cArgs = [];
                var stack = 0;
                if (args) {
                    for (var i = 0; i < args.length; i++) {
                        var converter = toC[argTypes[i]];
                        if (converter) {
                            if (stack === 0) stack = stackSave();
                            cArgs[i] = converter(args[i])
                        } else {
                            cArgs[i] = args[i]
                        }
                    }
                }
                var previousAsync = Asyncify.currData;
                var ret = func(...cArgs);

                function onDone(ret) {
                    runtimeKeepalivePop();
                    if (stack !== 0) stackRestore(stack);
                    return convertReturnValue(ret)
                }

                var asyncMode = opts?.async;
                runtimeKeepalivePush();
                if (Asyncify.currData != previousAsync) {
                    return Asyncify.whenDone().then(onDone)
                }
                ret = onDone(ret);
                if (asyncMode) return Promise.resolve(ret);
                return ret
            };
            var cwrap = (ident, returnType, argTypes, opts) => {
                var numericArgs = !argTypes || argTypes.every(type => type === "number" || type === "boolean");
                var numericRet = returnType !== "string";
                if (numericRet && numericArgs && !opts) {
                    return getCFunc(ident)
                }
                return (...args) => ccall(ident, returnType, argTypes, args, opts)
            };
            for (var base64ReverseLookup = new Uint8Array(123), i = 25; i >= 0; --i) {
                base64ReverseLookup[48 + i] = 52 + i;
                base64ReverseLookup[65 + i] = i;
                base64ReverseLookup[97 + i] = 26 + i
            }
            base64ReverseLookup[43] = 62;
            base64ReverseLookup[47] = 63;
            Fetch.init();
            {
                if (Module["noExitRuntime"]) noExitRuntime = Module["noExitRuntime"];
                if (Module["print"]) out = Module["print"];
                if (Module["printErr"]) err = Module["printErr"];
                if (Module["wasmBinary"]) wasmBinary = Module["wasmBinary"];
                if (Module["arguments"]) arguments_ = Module["arguments"];
                if (Module["thisProgram"]) thisProgram = Module["thisProgram"]
            }
            Module["ccall"] = ccall;
            Module["cwrap"] = cwrap;
            Module["getValue"] = getValue;
            var wasmImports = {
                c: _emscripten_fetch_free,
                e: _emscripten_is_main_browser_thread,
                b: _emscripten_resize_heap,
                f: _emscripten_sleep,
                d: _emscripten_start_fetch,
                a: _fd_write
            };
            var wasmExports = await createWasm();
            var ___wasm_call_ctors = wasmExports["h"];
            var _Add = Module["_Add"] = wasmExports["i"];
            var _HttpTest = Module["_HttpTest"] = wasmExports["j"];
            var _malloc = wasmExports["l"];
            var _free = wasmExports["m"];
            var __emscripten_stack_restore = wasmExports["n"];
            var __emscripten_stack_alloc = wasmExports["o"];
            var _emscripten_stack_get_current = wasmExports["p"];
            var dynCall_vi = Module["dynCall_vi"] = wasmExports["q"];
            var _asyncify_start_unwind = wasmExports["r"];
            var _asyncify_stop_unwind = wasmExports["s"];
            var _asyncify_start_rewind = wasmExports["t"];
            var _asyncify_stop_rewind = wasmExports["u"];

            function run() {
                if (runDependencies > 0) {
                    dependenciesFulfilled = run;
                    return
                }
                preRun();
                if (runDependencies > 0) {
                    dependenciesFulfilled = run;
                    return
                }

                function doRun() {
                    Module["calledRun"] = true;
                    if (ABORT) return;
                    initRuntime();
                    readyPromiseResolve(Module);
                    Module["onRuntimeInitialized"]?.();
                    postRun()
                }

                if (Module["setStatus"]) {
                    Module["setStatus"]("Running...");
                    setTimeout(() => {
                        setTimeout(() => Module["setStatus"](""), 1);
                        doRun()
                    }, 1)
                } else {
                    doRun()
                }
            }

            function preInit() {
                if (Module["preInit"]) {
                    if (typeof Module["preInit"] == "function") Module["preInit"] = [Module["preInit"]];
                    while (Module["preInit"].length > 0) {
                        Module["preInit"].shift()()
                    }
                }
            }

            preInit();
            run();
            moduleRtn = readyPromise;


            return moduleRtn;
        }
    );
})();
if (typeof exports === 'object' && typeof module === 'object') {
    module.exports = AI;
    // This default export looks redundant, but it allows TS to import this
    // commonjs style module.
    module.exports.default = AI;
} else if (typeof define === 'function' && define['amd'])
    define([], () => AI);

AI().then((Module) => {
        console.log("Module loaded successfully:", Module);
        const url = "https://jsonplaceholder.typicode.com/posts/1";
        let resultPtr;
        try {
            resultPtr = Module.ccall('HttpTest', 'number', ['string'], [url]);
            resultPtr.then(value => {
                console.log(`value is ${value}`)
                try {
                    console.log(`解析i16 ${Module.getValue(value, 'i16')}`)
                } catch (e) {
                    console.log(e)
                }
            }, reason => {
                console.log(`reason is ${reason}`)
            })
        } catch (error) {
            console.error("HttpTest 调用失败:", error);
        }


        // const httpResPtr = Module.exports.HttpTest(url);
        // const result = Module.ccall('HttpTest', ['number'], ['string'], ['https://jsonplaceholder.typicode.com/posts/1']);
        // const statusCode = readUint16(httpResPtr);
        // console.log("Status Code:", statusCode);
        // const result = Module._HttpTest(url);

        // const HttpTest = Module.cwrap('HttpTest', 'number', ['string']);
        // const memoryBuffer = Module.HEAPU8.buffer;
        // const dataView = new DataView(memoryBuffer);
        // const statusCode = dataView.getUint16(result, true); // 'true' 表示小端字节序
        // console.log("Status Code:", statusCode);
        // let statusCode = Module.HEAP16[result >> 1];
        // const result = Module.ccall('HttpTest', 'object', ['string'], ['https://jsonplaceholder.typicode.com/posts/1']);
        // let result = HttpTest("https://jsonplaceholder.typicode.com/posts/1");
        // if (Module.HEAP16) {
        //     const statusCode = Module.HEAP16[result >> 1];
        //     console.log("statusCode: ", statusCode);
        // } else {
        //     console.error("Module HEAP16 is not initialized.");
        // }
        // const statusCode = Module.HEAP16[result >> 1];
        // console.log("statusCode: ", statusCode);
    }).catch((error) => {
        console.error("Module failed to load:", error);
    });