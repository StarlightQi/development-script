import {defineConfig} from 'vite';
// @ts-ignore
import vue from '@vitejs/plugin-vue';
import copy from 'rollup-plugin-copy';
import AutoImport from 'unplugin-auto-import/vite'

export default defineConfig({
    plugins: [
        vue(),
        AutoImport({
            imports: ['vue'],
            dts: 'src/auto-imports.test.d.ts', // 生成自动导入的类型声明文件
        }),
        copy({
            targets: [
                {src: "manifest.json", dest: "dist"},
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
            fileName: (name) => `browser.${name}.js`,
            formats:['umd']
        },
        rollupOptions: {
            external: [],
            output: {
                globals: {},
                format: 'umd',
                name:"browser"
            }
        }
    }
});
