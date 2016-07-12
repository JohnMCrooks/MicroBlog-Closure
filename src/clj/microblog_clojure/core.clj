(ns microblog-clojure.core
  (:require [compojure.core :as c]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [hiccup.core :as h]
            [ring.adapter.jetty :as j]
            [ring.util.response :as r])
            
  (:gen-class))


(def ^:const port 3000)
(defonce server (atom nil))
(defonce messages (atom []))

(c/defroutes app
  (c/GET "/" request
    (h/html [:html
             [:head
              [:link {:rel "stylesheet" :href "page.css"}]]
             [:body
              [:form {:action "/add-message" :method "post"}
               [:input {:type "text" :placeholder "Enter Message" :name "mesage"}]
               [:button {:type "submit"} "Add message"]]]])))
            
             

(defn -main [& args]
  (when @server
    (.stop @server))
  (let [app(p/wrap-params app)]
    (reset! server
          (j/run-jetty ap {:port port :join? false}))))
 
