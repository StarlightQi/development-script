import {defineConfig} from 'vite';
// @ts-ignore
import vue from '@vitejs/plugin-vue';
import copy from 'rollup-plugin-copy';
import AutoImport from 'unplugin-auto-import/vite'

const main = {
    plugins: [
        vue(),
        AutoImport({
            imports: ['vue'],
            dts: 'src/auto-imports.test.d.ts', // 生成自动导入的类型声明文件
        }),
        copy({
            targets: [
                {src: "manifest.json", dest: "dist"},
                {src: "index.html", dest: "dist"},
                {src: "assets/*", dest: "dist/assets"},
            ],
            hook: 'writeBundle'
        }),
    ],
    define: {
        'process.env': {},
        'process.env.NODE_ENV': JSON.stringify('production')
    },
    build: {
        minify: 'terser',
        lib: {
            entry: "src/main.ts",
            name: "browser",
            fileName: (name: string) => `browser.${name}.js`,
            formats: ['umd']
        },
        rollupOptions: {
            external: [],
            output: {
                globals: {},
                format: 'umd',
                name: "browser"
            }
        }
    }
}

const devtools = {
    build: {
        rollupOptions: {
            input: {
                devtools: "src/chrome/devtools.ts",  // 独立入口
                background: "src/chrome/background.ts",  // 独立入口
            },
            output: {
                format: 'es',  // ES模块格式
                dir: 'dist/plugin',  // 输出目录
                entryFileNames: () => {
                    return '[name].js';  // 默认输出文件名
                },
            }
        }
    }
}

const loadScript = {
    build: {
        rollupOptions: {
            input: {
                load: "src/load.ts",  // 独立入口
            },
            output: {
                format: 'es',  // ES模块格式
                dir: 'dist/load',  // 输出目录
                entryFileNames: () => {
                    return '[name].js';  // 默认输出文件名
                },
            }
        }
    }
}

const dev = {
    plugins: [vue()],
    server: {
        host: 'localhost', // 本地开发服务器的主机名
        port: 3000,        // 开发服务器的端口号
        open: true,        // 自动在浏览器中打开
        // proxy: {
        //     // 代理设置，示例中将所有 /api 的请求代理到后端服务器
        //     '/api': {
        //         target: 'http://backend.local', // 后端服务器地址
        //         changeOrigin: true,
        //         rewrite: (path) => path.replace(/^\/api/, '')
        //     }
        // }
    },
    resolve: {
        alias: {
            '@': '/src' // 方便导入 src 目录下的模块
        }
    }
}


const popup = {
    plugins: [vue()],
    root: 'src/popup', // 设置项目的根目录\
    base: '/popup/',
    resolve: {
        alias: {
            '@': '/src' // 方便导入 src 目录下的模块
        }
    },
    build: {
        outDir: './../../dist/popup', // 指定输出目录
        rollupOptions: {
            input: {
                main:'src/popup/main.ts', // 明确指定入口文件
                index: 'src/popup/index.html',
            }
        }
    }
}
const configMap = {
    "devtools": devtools,
    "load": loadScript,
    "dev": dev,
    "popup":popup,
}

// @ts-ignore
export default defineConfig(({mode}) => {
    const config = configMap[mode]
    return config || main;
});
