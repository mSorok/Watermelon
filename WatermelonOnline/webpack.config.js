module.exports = {
    entry: {
        index: "./src/main/js/pages/MainPage.js"
    },
    devtool: 'source-map',
    cache: true,
    mode: "development",
    watch: true,
    output: {
        path: __dirname,
        filename: "./src/main/resources/static/built/[name].js"
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: [{
                    loader: "babel-loader",
                    options: {
                        presets: [
                            "@babel/preset-env",
                            "@babel/preset-react", {
                                "plugins": ["@babel/plugin-proposal-class-properties"]}]
                    }
                }]
            },
            {
                test: /\.scss$/,
                use: ["style-loader", "css-loader", "sass-loader", {
                    loader: "postcss-loader",
                    options: {
                        postcssOptions: {
                        plugins: function () {
                            return [
                                require("autoprefixer")
                            ];
                        }
                    }
                    }
                }]
            },
            {
                test: /\.(png|jp(e*)g|svg)$/,
                use: [{
                    loader: "url-loader"
                }]
            },
            {
                test: /\.md$/,
                use: 'raw-loader'
            }
        ]
    }
};

