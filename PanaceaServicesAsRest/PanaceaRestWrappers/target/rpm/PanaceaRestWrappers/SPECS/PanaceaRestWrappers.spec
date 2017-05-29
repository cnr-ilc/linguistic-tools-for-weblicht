%define __jar_repack 0
Name: PanaceaRestWrappers
Version: 1.0
Release: SNAPSHOT20170529103244
Summary: PanaceaRestWrappers
License: (c) null
Group: Applications/System
autoprov: yes
autoreq: yes
BuildArch: noarch
BuildRoot: /opt/java/app/linguistic-tools-for-weblicht/PanaceaServicesAsRest/PanaceaRestWrappers/target/rpm/PanaceaRestWrappers/buildroot

%description

%install
if [ -d $RPM_BUILD_ROOT ];
then
  mv /opt/java/app/linguistic-tools-for-weblicht/PanaceaServicesAsRest/PanaceaRestWrappers/target/rpm/PanaceaRestWrappers/tmp-buildroot/* $RPM_BUILD_ROOT
else
  mv /opt/java/app/linguistic-tools-for-weblicht/PanaceaServicesAsRest/PanaceaRestWrappers/target/rpm/PanaceaRestWrappers/tmp-buildroot $RPM_BUILD_ROOT
fi

%files

 "/usr/share/PanaceaRestWrappers/"
%dir %attr(700,ug,sg) "/var/lib/PanaceaRestWrappers/"
%dir %attr(700,ug,sg) "/var/log/PanaceaRestWrappers/"
%attr(755,-,-)  "/usr/bin/PanaceaRestWrappers"
%attr(755,-,-)  "/etc/init.d/PanaceaRestWrappers"
%config   "/etc/default/PanaceaRestWrappers"
%config   "/etc/clarind/PanaceaRestWrappers.yaml"

%pre
/usr/bin/getent group sg > /dev/null || /usr/sbin/groupadd sg
                            /usr/bin/getent passwd ug > /dev/null || /usr/sbin/useradd -r -d /var/lib/PanaceaRestWrappers -m -g sg ug

%post
chkconfig --add PanaceaRestWrappers;
                            if [ $1 -eq 0 ]; then
                            /sbin/service PanaceaRestWrappers start
                            elif [ $1 -ge 1 ]; then
                            /sbin/service PanaceaRestWrappers restart
                            fi

%preun
if [ $1 -eq 0 ] ; then
                            /sbin/service PanaceaRestWrappers stop;chkconfig --del PanaceaRestWrappers
                            fi
