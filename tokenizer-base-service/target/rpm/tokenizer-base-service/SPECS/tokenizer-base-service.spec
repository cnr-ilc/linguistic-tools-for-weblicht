%define __jar_repack 0
Name: tokenizer-base-service
Version: 1.0
Release: SNAPSHOT20161128161716
Summary: tokenizer-base-service
License: (c) null
Group: Applications/System
autoprov: yes
autoreq: yes
BuildArch: noarch
BuildRoot: /opt/java/app/linguistic-tools-for-weblicht/tokenizer-base-service/target/rpm/tokenizer-base-service/buildroot

%description

%install
if [ -d $RPM_BUILD_ROOT ];
then
  mv /opt/java/app/linguistic-tools-for-weblicht/tokenizer-base-service/target/rpm/tokenizer-base-service/tmp-buildroot/* $RPM_BUILD_ROOT
else
  mv /opt/java/app/linguistic-tools-for-weblicht/tokenizer-base-service/target/rpm/tokenizer-base-service/tmp-buildroot $RPM_BUILD_ROOT
fi

%files

 "/usr/share/tokenizer-base-service/"
%dir %attr(700,su,sg) "/var/lib/tokenizer-base-service/"
%dir %attr(700,su,sg) "/var/log/tokenizer-base-service/"
%attr(755,-,-)  "/usr/bin/tokenizer-base-service"
%attr(755,-,-)  "/etc/init.d/tokenizer-base-service"
%config   "/etc/default/tokenizer-base-service"
%config   "/etc/clarind/tokenizer-base-service.yaml"

%pre
/usr/bin/getent group sg > /dev/null || /usr/sbin/groupadd sg
                            /usr/bin/getent passwd su > /dev/null || /usr/sbin/useradd -r -d /var/lib/tokenizer-base-service -m -g sg su

%post
chkconfig --add tokenizer-base-service;
                            if [ $1 -eq 0 ]; then
                            /sbin/service tokenizer-base-service start
                            elif [ $1 -ge 1 ]; then
                            /sbin/service tokenizer-base-service restart
                            fi

%preun
if [ $1 -eq 0 ] ; then
                            /sbin/service tokenizer-base-service stop;chkconfig --del tokenizer-base-service
                            fi
