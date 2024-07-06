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
                entryFileNames:()=>{
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
                entryFileNames:()=>{
                    return '[name].js';  // 默认输出文件名
                },
            }
        }
    }
}


const configMap={
    "devtools":devtools,
    "load":loadScript
}

// @ts-ignore
export default defineConfig(({mode}) => {
    const config=configMap[mode]
    return config||main;
});
