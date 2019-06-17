#import "WhatsappContactsPlugin.h"
#import <whatsapp_contacts/whatsapp_contacts-Swift.h>

@implementation WhatsappContactsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftWhatsappContactsPlugin registerWithRegistrar:registrar];
}
@end
