FROM java:8

# Locale setup
RUN apt-get update && \
    apt-get install -yq locales ca-certificates wget sudo && \
    rm -rf /var/lib/apt/lists/* && \
    localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8

ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

# Add dev user
RUN useradd user -d /home/user -s /bin/bash && \
    cp -r /etc/skel /home/user && \
    chown user.user -R /home/user && \
    echo 'user ALL=(ALL:ALL) NOPASSWD: ALL' >> /etc/sudoers

# Dependencies
RUN apt-get update && apt-get install -yq \
                                      tmux \
                                      vim \
                                      zip \
                                      xz-utils \
                                      curl \
                                      build-essential \
                                      protobuf-compiler \
                                      rlwrap



USER user
WORKDIR /home/user
COPY files/dev-entrypoint.sh .
RUN sudo chmod +x /home/user/dev-entrypoint.sh

EXPOSE 5050
