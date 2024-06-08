import {defineConfig} from 'vite';
// @ts-ignore
import vue from '@vitejs/plugin-vue';
import copy from 'rollup-plugin-copy';


export default defineConfig({
    plugins: [
        vue(),
        copy({
            targets: [
                {src: "manifest.json", dest: "dist"},
                {src: "assets/*", dest: "dist/assets"},
            ],
            hook: 'writeBundle'
        })
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
