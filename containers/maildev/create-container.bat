docker run --name maildev -p 1080:1080 -p 1025:1025 -e MAILDEV_WEB_USER=admin -e MAILDEV_WEB_PASS=admin -e MAILDEV_MAIL_DIRECTORY=/home/maildev -v .\volume/data:/home/maildev:rw maildev/maildev